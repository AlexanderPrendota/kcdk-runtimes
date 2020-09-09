package com.kotlin.aws.runtime.utils

import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal inline fun <reified T : Any> Project.myExt(name: String) = myExt[name] as T

internal val Project.myExt: ExtraPropertiesExtension
    get() = myExtByName("ext")

internal inline fun <reified T : Any> Project.myExtByName(name: String): T = extensions.getByName(name) as T

internal val Project.mySourceSets: SourceSetContainer
    get() = myExtByName("sourceSets")

val SourceSet.kotlin: SourceDirectorySet
    get() =
        (this as HasConvention)
            .convention
            .getPlugin(KotlinSourceSet::class.java)
            .kotlin

