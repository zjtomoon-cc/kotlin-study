package async

import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {
    val format = "%-10s%-20s%-10s"
    println(String.format(format,"code","Temperature","Delay"))

    val time = measureTimeMillis {
        val airportCodes = listOf("LAX", "SFO", "PDX", "SEA")
        val airportData:List<Airport> =
            airportCodes.mapNotNull { airportCodes ->
                Airport.getAirportData(airportCodes)
            }
        airportData.forEach {   anAirport ->
            println(String.format(format,anAirport.code,anAirport.weather.temperature.get(0),anAirport.delay))
        }
    }

    println("Time taken $time ms")
}