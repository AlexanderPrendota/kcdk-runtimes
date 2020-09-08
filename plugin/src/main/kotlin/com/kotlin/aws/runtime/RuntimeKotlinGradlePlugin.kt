@file:Suppress("unused")

package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.tasks.ConfigureGraal
import org.gradle.api.Plugin
import org.gradle.api.Project


class RuntimeKotlinGradlePlugin : Plugin<Project> {
    @ExperimentalUnsignedTypes
    override fun apply(target: Project) {
        ConfigureGraal.configure(target)
    }
}
