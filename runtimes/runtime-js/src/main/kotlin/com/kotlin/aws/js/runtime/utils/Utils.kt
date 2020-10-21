package com.kotlin.aws.js.runtime.utils

private external val process: Process

private external interface Process {
    val env: dynamic
}

internal fun getEnv(name: String): String? = process.env[name].unsafeCast<String?>()