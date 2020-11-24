package com.kotlin.aws.native.plugin.utils

import com.kotlin.aws.native.plugin.dsl.AwsLambdaRuntime
import org.gradle.api.Project
import org.gradle.api.Task

internal val Project.runtime
    get() = this.extensions.findByName("runtime") as AwsLambdaRuntime? ?: this.extensions.create(
        "runtime",
        AwsLambdaRuntime::class.java
    )

fun Project.runtime(configure: AwsLambdaRuntime.() -> Unit) {
    runtime.apply(configure)
}

internal fun <T : Task> Project.createTask(klass: Class<T>, name: String? = null) = this.tasks.create(
    name ?: klass.simpleName[0].toLowerCase() + klass.simpleName.substring(1),
    klass
)