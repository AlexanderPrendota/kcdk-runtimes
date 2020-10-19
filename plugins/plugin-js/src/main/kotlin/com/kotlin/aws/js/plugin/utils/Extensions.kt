package com.kotlin.aws.js.plugin.utils

import com.kotlin.aws.js.plugin.dsl.RuntimePluginExtension
import org.gradle.api.Project
import org.gradle.api.Task

internal val Project.runtime
    get() = this.extensions.findByName("runtime") as RuntimePluginExtension? ?: this.extensions.create(
        "runtime",
        RuntimePluginExtension::class.java
    )

internal fun <T : Task> Project.createTask(klass: Class<T>, name: String? = null) = this.tasks.create(
    name ?: klass.simpleName[0].toLowerCase() + klass.simpleName.substring(1),
    klass
)