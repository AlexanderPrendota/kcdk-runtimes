package com.kotlin.aws.runtime.objects

data class AwsLambdaInvocation(
    val context: LambdaContext,
    val apiGatewayProxyRequest: String
)
