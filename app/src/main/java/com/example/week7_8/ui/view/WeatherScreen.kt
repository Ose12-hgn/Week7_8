package com.example.week7_8.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week7_8.R
import com.example.week7_8.ui.model.Weather
import com.example.week7_8.ui.viewmodel.WeatherUiState
import com.example.week7_8.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(vm: WeatherViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Biru tua
                        Color(0xFF3B82F6)  // Biru cerah
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                modifier = Modifier.padding(16.dp),
                onSearch = { cityName -> vm.fetchWeatherData(cityName) }
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (val state = uiState) {
                    is WeatherUiState.Initial -> InitialView()
                    is WeatherUiState.Loading -> CircularProgressIndicator(color = Color.White)
                    is WeatherUiState.Success -> WeatherDetails(data = state.data)
                    is WeatherUiState.Error -> ErrorView(message = state.message)
                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Enter city name...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                focusedContainerColor = Color.White.copy(alpha = 0.3f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedPlaceholderColor = Color.LightGray,
                focusedPlaceholderColor = Color.LightGray,
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onSearch(text) },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Search")
        }
    }
}

@Composable
fun InitialView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.White,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for a city to get started",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorView(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        // ✅ FIXED: Gunakan icon dari Material Icons karena ic_error_triangle tidak ada
        Icon(
            imageVector = Icons.Default.Search, // Ganti dengan icon yang tersedia
            contentDescription = "Error Icon",
            modifier = Modifier.size(80.dp),
            tint = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Oops! Something went wrong", color = Color.White, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, color = Color.LightGray, fontSize = 18.sp)
    }
}

@Composable
fun WeatherDetails(data: Weather) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            Text(text = data.cityName, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = data.condition, fontSize = 32.sp, color = Color.White)
                    Text(
                        text = data.temperature,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = data.pandaImageRes),
                    contentDescription = "Panda",
                    modifier = Modifier.size(150.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // ✅ FIXED: Menggunakan drawable yang tersedia
            val details = listOf(
                "HUMIDITY" to (data.humidity to R.drawable.WaterDrop),      // Ganti icon_humidity
                "WIND" to (data.windSpeed to R.drawable.Wind),              // Sesuai dengan yang ada
                "FEELS LIKE" to (data.feelsLike to R.drawable.Temperature), // Ganti icon_feels_like
                "RAIN FALL" to (data.rainFall to R.drawable.Rainy),         // Ganti icon_rain
                "PRESSURE" to (data.pressure to R.drawable.devices),        // Ganti icon_pressure (atau cari icon lain)
                "CLOUDS" to (data.cloudiness to R.drawable.cloud)           // Sesuai dengan yang ada
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(240.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(details) { (title, pair) ->
                    val (value, icon) = pair
                    InfoCard(title = title, value = value, iconRes = icon)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // ✅ FIXED: Menggunakan drawable yang tersedia
                SunInfo(
                    title = "SUNRISE",
                    time = data.sunriseTime,
                    iconRes = R.drawable.Sunrise  // Sesuai dengan yang ada
                )
                SunInfo(
                    title = "SUNSET",
                    time = data.sunsetTime,
                    iconRes = R.drawable.Sunset   // Sesuai dengan yang ada
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, iconRes: Int) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = Color.LightGray, fontSize = 12.sp)
        }
    }
}

@Composable
fun SunInfo(title: String, time: String, iconRes: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, color = Color.LightGray, fontSize = 14.sp)
            Text(text = time, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}