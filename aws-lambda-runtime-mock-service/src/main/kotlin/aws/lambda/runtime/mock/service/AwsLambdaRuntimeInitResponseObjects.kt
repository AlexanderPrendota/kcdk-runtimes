package aws.lambda.runtime.mock.service

data class AwsLambdaRuntimeInitResponse(
  var path: String = "",
  var httpMethod: String = "GET",
  var headers: Map<String, String> = emptyMap(),
  var queryStringParameters: Map<String, String> = emptyMap(),
  var body: String = ""
)
