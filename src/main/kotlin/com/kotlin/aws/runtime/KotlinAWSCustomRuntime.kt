package com.kotlin.aws.runtime

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kotlin.aws.runtime.LambdaEnvironment.DEADLINE_HEADER_NAME
import com.kotlin.aws.runtime.LambdaEnvironment.INVOKED_FUNCTION_ARN
import com.kotlin.aws.runtime.LambdaEnvironment.REQUEST_HEADER_NAME
import com.kotlin.aws.runtime.client.LambdaHttpClient
import com.kotlin.aws.runtime.handler.LambdaInvocationHandler
import com.kotlin.aws.runtime.objects.ApiGatewayProxyRequest
import com.kotlin.aws.runtime.objects.AwsLambdaInvocation
import com.kotlin.aws.runtime.objects.LambdaContext
import java.net.http.HttpResponse
import java.util.logging.Logger

val log: Logger = Logger.getLogger("Kotlin Custom Runtime")

fun main() {
    initRuntime()
}

internal fun initRuntime() {
    log.info("Init Kotlin GraalVM Runtime.")
    while (true) {
        initLambdaInvocation { requestId, awsLambdaInvocation ->
            handleLambdaInvocation(requestId, awsLambdaInvocation)
        }
    }
}


private fun initLambdaInvocation(handle: (String, ApiGatewayProxyRequest) -> Unit) {
    log.info("Create lambda invocation..")
    try {
        val (requestId, apiGatewayProxyRequest) = createLambdaInvocation()
        log.info("Get the invocation. Request ID: $requestId")
        handle(requestId, apiGatewayProxyRequest)
    } catch (t: Throwable) {
        t.printStackTrace()
        LambdaHttpClient.postInitError(t.message)
    }
}

private fun createLambdaInvocation(): AwsLambdaInvocation {
    val response = LambdaHttpClient.init()
    val requestId = response.headers().firstValue(REQUEST_HEADER_NAME).orElse(null)
        ?: error("Header: $REQUEST_HEADER_NAME was not found")
    val apiGatewayProxyRequest = jacksonObjectMapper().readValue(response.body(), ApiGatewayProxyRequest::class.java)
    printContext(requestId, response, apiGatewayProxyRequest)
    return AwsLambdaInvocation(requestId, apiGatewayProxyRequest)
}

private fun printContext(
    requestId: String,
    response: HttpResponse<String>,
    apiGatewayProxyRequest: ApiGatewayProxyRequest
) {
    val deadLineTime = response.headers().firstValue(DEADLINE_HEADER_NAME).orElse("0").toLong()
    val invokedFuncArn = response.headers().firstValue(INVOKED_FUNCTION_ARN).orElse(null)
    val context = LambdaContext(requestId, deadLineTime, invokedFuncArn)
    log.info("Response headers: ${response.headers()}")
    log.info("Parsed body: $apiGatewayProxyRequest")
    log.info("Response body: ${response.body()}")
    log.info("Lambda context: $context")
}


private fun handleLambdaInvocation(requestId: String, apiGatewayProxyRequest: ApiGatewayProxyRequest) {
    try {
        val result = LambdaInvocationHandler.handleInvocation(apiGatewayProxyRequest)
        LambdaHttpClient.invoke(requestId, result)
    } catch (t: Throwable) {
        t.printStackTrace()
        LambdaHttpClient.postInvokeError(requestId, t.message)
    }
}


