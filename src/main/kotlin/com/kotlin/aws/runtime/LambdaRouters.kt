package com.kotlin.aws.runtime

object LambdaRouters {
  private const val RUNTIME_DATE = "2018-06-01"
  private val LOCAL_PORT = System.getenv("AWS_API_GATEWAY_PORT")?.toIntOrNull() ?: 8800
  private val RUNTIME_API = System.getenv("AWS_LAMBDA_RUNTIME_API") ?: "localhost:$LOCAL_PORT"
  private val RUNTIME_BASE_URL = "http://$RUNTIME_API/$RUNTIME_DATE/runtime"

  const val REQUEST_HEADER_NAME = "lambda-runtime-aws-request-id"
  val HANDLER_CLASS: String = System.getenv("_HANDLER") ?: error("No handler method provided")
  val LAMBDA_TASK_ROOT: String = System.getenv("LAMBDA_TASK_ROOT") ?: ""

  // Lambda API
  val RUNTIME_INITIALIZE_ERROR = "$RUNTIME_BASE_URL/init/error"
  val INVOKE_NEXT = "$RUNTIME_BASE_URL/invocation/next"
  fun getInvocationResponse(requestId: String) = "$RUNTIME_BASE_URL/invocation/$requestId/response"
  fun getInvocationError(requestId: String) = "$RUNTIME_BASE_URL/invocation/$requestId/error"
}