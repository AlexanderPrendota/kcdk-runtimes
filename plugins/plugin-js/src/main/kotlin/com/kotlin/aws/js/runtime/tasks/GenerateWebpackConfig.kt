package com.kotlin.aws.js.runtime.tasks

import com.kotlin.aws.js.runtime.utils.Groups
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateWebpackConfig : DefaultTask() {

    init {
        group = Groups.`js setup`
    }

    @TaskAction
    fun generate() {
        val dir = File(project.rootDir, "webpack.config.d")
        dir.mkdirs()
        with(File(dir, "webpack-config.js")) {
            writeText("config.target = 'node'")
        }
    }

}