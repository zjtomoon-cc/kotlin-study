package async

import com.beust.klaxon.*

import java.net.URL

class Weather(@Json(name = "Temp") val temperature: Array<String>)

class Airport(
    @Json(name = "IATA") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "Delay") val delay:Boolean,
    @Json(name = "Weather") val weather: Weather) {
    companion object{
        fun getAirportData(code: String): Airport? {
            var url = "https://soa.smext.faa.gov/asws/api/airport/status/${code}"
            return Klaxon().parse<Airport>(URL(url).readText())
        }
    }
}

