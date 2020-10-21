package com.kotlin.aws.js.plugin.tasks

import com.kotlin.aws.js.plugin.utils.Groups
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class BuildNodeJsRuntimeLambda : DefaultTask() {

    init {
        group = Groups.js
        description = "Build executable JS file with your Lambda"
    }

    @TaskAction
    fun generate() {
        // nothing special here for now
    }

}