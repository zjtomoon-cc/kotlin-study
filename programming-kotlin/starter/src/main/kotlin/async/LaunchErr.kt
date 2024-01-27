package async

import kotlinx.coroutines.*

fun main() = runBlocking {
    try {
        val airportCodes = listOf("LAX", "SFO", "PDX", "SEA")
        val jobs: List<Job> = airportCodes.map { anAirportCode ->
            launch(Dispatchers.IO + SupervisorJob()) {
                val airport = Airport.getAirportData(anAirportCode)
                println("${airport?.code} delay:${airport?.delay}")
            }
        }
        jobs.forEach {it.join()}
        jobs.forEach { println("Cancelled:${it.isCancelled}")}
    }catch (ex:Exception) {
        println("Error: ${ex.message}")
    }
}

// 使用launch 启动的协程不会将异常传播给它们的调用方。