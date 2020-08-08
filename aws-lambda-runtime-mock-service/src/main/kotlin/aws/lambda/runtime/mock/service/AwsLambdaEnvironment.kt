package aws.lambda.runtime.mock.service

/**
 * Runtime environment
 * https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html#configuration-envvars-runtime
 */
internal object AwsLambdaEnvironment {
  const val REQUEST_HEADER_NAME = "Lambda-Runtime-Aws-Request-Id"
  const val DEADLINE_HEADER_NAME = "Lambda-Runtime-Deadline-Ms"
  const val INVOKED_FUNCTION_ARN = "Lambda-Runtime-Invoked-Function-Arn"
  val HANDLER_CLASS: String = System.getenv("_HANDLER") ?: error("No handler method provided")
  val LAMBDA_TASK_ROOT: String = System.getenv("LAMBDA_TASK_ROOT") ?: ""

  // Additional
  val FUNCTION_VERSION: String = System.getenv("AWS_LAMBDA_FUNCTION_VERSION") ?: "-1"
  val LOG_GROUP_NAME: String = System.getenv("AWS_LAMBDA_LOG_GROUP_NAME") ?: "local"
  val MEMORY_LIMIT: Int = System.getenv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE")?.toIntOrNull() ?: 1024
  val FUNCTION_NAME: String = System.getenv("AWS_LAMBDA_FUNCTION_NAME")
  val LOG_STREAM_NAME: String = System.getenv("AWS_LAMBDA_LOG_STREAM_NAME") ?: "local"

}