package com.kotlin.aws.js.runtime.tasks

import com.kotlin.aws.js.runtime.utils.Groups
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class BuildNodeJsRuntimeLambda : DefaultTask() {

    init {
        group = Groups.aws
    }

    @TaskAction
    fun generate() {
        // nothing special here for now
    }

}