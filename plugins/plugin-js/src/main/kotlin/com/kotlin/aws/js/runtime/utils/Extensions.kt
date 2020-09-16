package com.kotlin.aws.js.runtime.utils

import com.kotlin.aws.js.runtime.dsl.RuntimePluginExtension
import org.gradle.api.Project
import org.gradle.api.Task

internal val Project.runtime
    get() = this.extensions.findByName("runtime") as RuntimePluginExtension? ?: this.extensions.create("runtime", RuntimePluginExtension::class.java)

internal fun Project.getTask(name: String): Task {
    val task = this.tasks.findByName(name)
    require(task != null) {
        "Kotlin JS Runtime can not find task '$name'"
    }
    return task
}