package com.kotlin.aws.runtime.utils

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

internal inline fun <reified T : Any> Project.myExtByName(name: String): T = extensions.getByName(name) as T

internal val Project.mySourceSets: SourceSetContainer
    get() = myExtByName("sourceSets")

internal val Project.myKtSourceSetFiles: Set<File>
    get() = mySourceSets.asMap["main"]!!.allSource.files.filter { it.extension == "kt" }.toSet()
