package xyz.ksharma.circuit_demo

import kotlinx.coroutines.delay

class CityListRepository {
    companion object {
        private val CITIES = listOf(
            "Sydney",
            "Melbourne",
            "New York",
            "San Francisco",
            "Delhi",
            "Mumbai",
            "Bangalore",
            "Chennai",
            "Kolkata",
            "Los Angeles",
            "Tokyo",
            "Osaka",
            "Chicago",
            "Seattle",
        )
    }

    suspend fun getAllCities(): List<String> {
        delay(1000)
        return CITIES
    }

    suspend fun searchCity(searchQuery: String): List<String> {
        delay(1000)
        return CITIES.filter { it.contains(searchQuery, ignoreCase = true) }
    }
}
