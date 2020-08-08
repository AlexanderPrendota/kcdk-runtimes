package com.kotlin.aws.runtime.objects

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ApiGatewayProxyRequest(
    var path: String = "",
    var httpMethod: String = "GET",
    var headers: Map<String, String> = emptyMap(),
    var queryStringParameters: Map<String, String> = emptyMap(),
    var body: String = ""
)

data class AwsLambdaInvocation(
    val requestId: String,
    val apiGatewayProxyRequest: ApiGatewayProxyRequest
)