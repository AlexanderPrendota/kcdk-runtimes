package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.utils.getEnv

internal object LambdaRouters {
    private const val RUNTIME_DATE = "2018-06-01"
    private val LOCAL_PORT = getEnv("AWS_API_GATEWAY_PORT")?.toIntOrNull() ?: 8080
    private val RUNTIME_API = getEnv("AWS_LAMBDA_RUNTIME_API") ?: "localhost:$LOCAL_PORT"
    val RUNTIME_BASE_URL = "http://$RUNTIME_API/$RUNTIME_DATE/runtime"
}

/**
 * Runtime environment
 * https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html#configuration-envvars-runtime
 */
internal object LambdaEnvironment {
    const val REQUEST_HEADER_NAME = "lambda-runtime-aws-request-id"
    const val DEADLINE_HEADER_NAME = "lambda-runtime-deadline-ms"
    const val INVOKED_FUNCTION_ARN = "lambda-runtime-invoked-function-arn"
    //val HANDLER_CLASS: String = getEnv("_HANDLER") ?: error("No handler method provided")
    //val LAMBDA_TASK_ROOT: String = getEnv("LAMBDA_TASK_ROOT") ?: ""

    // Additional
    val FUNCTION_VERSION: String = getEnv("AWS_LAMBDA_FUNCTION_VERSION") ?: "-1"
    val LOG_GROUP_NAME: String = getEnv("AWS_LAMBDA_LOG_GROUP_NAME") ?: "local"
    val MEMORY_LIMIT: Int = getEnv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE")?.toIntOrNull() ?: 1024
    val FUNCTION_NAME: String = getEnv("AWS_LAMBDA_FUNCTION_NAME") ?: ""
    val LOG_STREAM_NAME: String = getEnv("AWS_LAMBDA_LOG_STREAM_NAME") ?: "local"
}
