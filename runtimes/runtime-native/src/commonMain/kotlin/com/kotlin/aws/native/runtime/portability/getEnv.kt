package com.kotlin.aws.native.runtime.portability

/**
 * Read value of environment [variable].
 */
expect fun getEnv(variable: String): String?