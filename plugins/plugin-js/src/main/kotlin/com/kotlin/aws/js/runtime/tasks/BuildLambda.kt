package com.kotlin.aws.js.runtime.tasks

import com.kotlin.aws.js.runtime.utils.runtime
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class BuildLambda : DefaultTask() {

    @get:Input
    val extension = project.runtime

    init {
        group = "aws"
    }

    @TaskAction
    fun generate() {
        val dir = if (extension.outputDir != null)
            File(extension.outputDir!!)
        else
            File(project.buildDir, "distributions")

        dir.mkdirs()
        with(File(dir, "bootstrap")) {
            writeText("""
                # just a placeholder, not tested yet
                node sample.js
            """.trimIndent())
        }
    }

}