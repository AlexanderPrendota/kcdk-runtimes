package com.kotlin.aws.runtime.objects

data class ApiGatewayProxyRequest(
  var path: String,
  var httpMethod: String,
  var headers: Map<String, String>,
  var queryStringParameters: Map<String, String>,
  var body: String
)

data class AwsLambdaInvocation(
  val requestId: String,
  val apiGatewayProxyRequest: ApiGatewayProxyRequest
)