package com.kotlin.aws.runtime.objects

data class ApiGatewayProxyRequest(
    val body: String? = null,
    val headers: Map<String, String> = emptyMap(),
    val httpMethod: String = "GET",
    val isBase64Encoded: Boolean = false,
    val multiValueHeaders: Map<String, List<String>>? = null,
    val multiValueQueryStringParameters: Map<String, List<String>>? = null,
    val path: String = "/",
    val pathParameters: Map<String, String>? = null,
    val queryStringParameters: Map<String, String>? = null,
    val requestContext: AwsLambdaRequestContext? = null,
    val resource: String? = null,
    val stageVariables: Map<String, String>? = null
)

data class AwsLambdaRequestContext(
    val accountId: String,
    val apiId: String,
    val domainName: String,
    val domainPrefix: String,
    val extendedRequestId: String,
    val httpMethod: String,
    val identity: AwsLambdaRequestIdentity,
    val path: String,
    val protocol: String,
    val requestId: String,
    val requestTime: String,
    val requestTimeEpoch: Long,
    val resourceId: String,
    val resourcePath: String,
    val stage: String
)

data class AwsLambdaRequestIdentity(
    val accessKey: Any?,
    val accountId: Any?,
    val caller: Any?,
    val cognitoAuthenticationProvider: Any?,
    val cognitoAuthenticationType: Any?,
    val cognitoIdentityId: Any?,
    val cognitoIdentityPoolId: Any?,
    val principalOrgId: Any?,
    val sourceIp: String,
    val user: Any?,
    val userAgent: String,
    val userArn: Any?
)

data class AwsLambdaInvocation(
    val context: LambdaContext,
    val apiGatewayProxyRequest: String
)