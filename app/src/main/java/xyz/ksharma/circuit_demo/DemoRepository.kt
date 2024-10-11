package xyz.ksharma.circuit_demo

import kotlinx.coroutines.delay

class DemoRepository {
    companion object {
        private const val EMAIL = "email@email.com"
    }

    suspend fun getEmails(): List<String> {
        delay(1000)
        return listOf(EMAIL, EMAIL)
    }

    fun getEmail(data: String) = if(data.isNotBlank()) EMAIL else "Error"
}
