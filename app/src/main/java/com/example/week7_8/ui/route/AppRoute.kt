package com.example.week7_8.ui.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.week7_8.ui.view.WeatherScreen

// Definisikan layar-layar kita
enum class WeatherAppView(val title: String) {
    WeatherHome("Weather App")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoute() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = WeatherAppView.WeatherHome.title) }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = WeatherAppView.WeatherHome.name
        ) {
            // Satu-satunya layar kita saat ini
            composable(route = WeatherAppView.WeatherHome.name) {
                WeatherScreen()
            }
            // Anda bisa tambahkan layar lain di sini nanti
        }
    }
}
