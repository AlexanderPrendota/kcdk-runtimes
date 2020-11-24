import com.kotlin.aws.native.runtime.objects.LambdaContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

suspend fun spaceXHandler(context: LambdaContext, request: String): String {
    return Json.encodeToString(getLaunches().map { it.rocket })
}