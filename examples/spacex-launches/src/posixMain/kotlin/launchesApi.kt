import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val LAUNCHES_ENDPOINT = "https://api.spacexdata.com/v3/launches"

@Serializable
data class RocketLaunch(
        @SerialName("flight_number")
        val flightNumber: Int,
        @SerialName("mission_name")
        val missionName: String,
        @SerialName("launch_year")
        val launchYear: Int,
        @SerialName("launch_date_utc")
        val launchDateUTC: String,
        @SerialName("rocket")
        val rocket: Rocket,
        @SerialName("details")
        val details: String?,
        @SerialName("launch_success")
        val launchSuccess: Boolean?,
        @SerialName("links")
        val links: Links
)

@Serializable
data class Rocket(
        @SerialName("rocket_id")
        val id: String,
        @SerialName("rocket_name")
        val name: String,
        @SerialName("rocket_type")
        val type: String
)

@Serializable
data class Links(
        @SerialName("mission_patch")
        val missionPatchUrl: String?,
        @SerialName("article_link")
        val articleUrl: String?
)

suspend fun getLaunches(): List<RocketLaunch> {
    val client = HttpClient(Curl) {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }
    return client.get(LAUNCHES_ENDPOINT)
}