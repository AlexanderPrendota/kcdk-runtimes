import com.kotlin.aws.native.runtime.objects.LambdaContext

suspend fun helloWorldHandler(context: LambdaContext, request: String): String =
    "Hello World!"