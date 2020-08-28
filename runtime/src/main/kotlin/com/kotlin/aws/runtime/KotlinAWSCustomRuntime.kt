package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.LambdaEnvironment.DEADLINE_HEADER_NAME
import com.kotlin.aws.runtime.LambdaEnvironment.INVOKED_FUNCTION_ARN
import com.kotlin.aws.runtime.LambdaEnvironment.REQUEST_HEADER_NAME
import com.kotlin.aws.runtime.client.LambdaHTTPClient
import com.kotlin.aws.runtime.objects.AwsLambdaInvocation
import com.kotlin.aws.runtime.objects.LambdaContext
import java.net.http.HttpResponse
import java.util.logging.Logger

val log: Logger = Logger.getLogger("Kotlin Custom Runtime")

fun main() {
    log.info("Init Kotlin GraalVM Runtime.")
    while (true) {
        initLambdaInvocation { requestId, awsLambdaInvocation ->
            Adapter.handleLambdaInvocation(requestId, awsLambdaInvocation)
        }
    }
}

private fun initLambdaInvocation(handle: (requestId: String, apiGatewayProxyRequest: String) -> Unit) {
    log.info("Create lambda invocation..")
    try {
        val (requestId, apiGatewayProxyRequest) = createLambdaInvocation()
        log.info("Get the invocation. Request ID: $requestId")
        handle(requestId, apiGatewayProxyRequest)
    } catch (t: Throwable) {
        t.printStackTrace()
        LambdaHTTPClient.postInitError(t.message)
    }
}

private fun createLambdaInvocation(): AwsLambdaInvocation {
    val response = LambdaHTTPClient.init()
    val requestId = response.headers().firstValue(REQUEST_HEADER_NAME).orElse(null)
        ?: error("Header: $REQUEST_HEADER_NAME was not found")
    val apiGatewayProxyRequest = response.body()
    printContext(requestId, response, apiGatewayProxyRequest)
    return AwsLambdaInvocation(requestId, apiGatewayProxyRequest)
}

private fun printContext(
    requestId: String,
    response: HttpResponse<String>,
    apiGatewayProxyRequest: String
) {
    val deadLineTime = response.headers().firstValue(DEADLINE_HEADER_NAME).orElse("0").toLong()
    val invokedFuncArn = response.headers().firstValue(INVOKED_FUNCTION_ARN).orElse(null)
    val context = LambdaContext(requestId, deadLineTime, invokedFuncArn)
    log.info("Response headers: ${response.headers()}")
    log.info("Parsed body: $apiGatewayProxyRequest")
    log.info("Response body: ${response.body()}")
    log.info("Lambda context: $context")
}


