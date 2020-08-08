package aws.lambda.runtime.mock.service

data class AwsLambdaRuntimeInitResponse(
    val body: String = "",
    val headers: Map<String, String> = emptyMap(),
    val httpMethod: String = "GET",
    val isBase64Encoded: Boolean,
    val multiValueHeaders: Map<String, List<String>>? = null,
    val multiValueQueryStringParameters: Any,
    val path: String,
    val pathParameters: Map<String, String>? = null,
    val queryStringParameters: Map<String, String>? = null,
    val requestContext: RequestContext,
    val resource: String,
    val stageVariables: Map<String, String>? = null
)

data class RequestContext(
    val accountId: String,
    val apiId: String,
    val domainName: String,
    val domainPrefix: String,
    val extendedRequestId: String,
    val httpMethod: String,
    val identity: Identity,
    val path: String,
    val protocol: String,
    val requestId: String,
    val requestTime: String,
    val requestTimeEpoch: Long,
    val resourceId: String,
    val resourcePath: String,
    val stage: String
)

data class Identity(
    val accessKey: Any,
    val accountId: Any,
    val caller: Any,
    val cognitoAuthenticationProvider: Any,
    val cognitoAuthenticationType: Any,
    val cognitoIdentityId: Any,
    val cognitoIdentityPoolId: Any,
    val principalOrgId: Any,
    val sourceIp: String,
    val user: Any,
    val userAgent: String,
    val userArn: Any
)