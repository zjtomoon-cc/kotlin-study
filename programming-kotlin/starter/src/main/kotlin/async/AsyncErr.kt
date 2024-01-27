package async

import kotlinx.coroutines.*

fun main() = runBlocking {
        val airportCodes = listOf("LAX", "SFO", "PDX", "SEA")
        val airportData = airportCodes.map { anAirportCode ->
            async (Dispatchers.IO + SupervisorJob()) {
                Airport.getAirportData(anAirportCode)
            }
        }

    for (anAirportData in airportData) {
        try {
            val airport = anAirportData.await()
            println("${airport?.code}  ${airport?.delay}")
        }catch (ex:Exception) {
            println("Error: ${ex.message?.substring(0..28)}")
        }
    }

}
