package com.kotlin.aws.native.runtime.portability

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual fun getEnv(variable: String): String? =
    getenv(variable)?.toKString()