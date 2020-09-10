@file:Suppress("unused")

package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.dsl.RuntimePluginExtension
import com.kotlin.aws.runtime.dsl.runtime
import com.kotlin.aws.runtime.tasks.ConfigureGraal
import com.kotlin.aws.runtime.tasks.GenerateAdapter
import com.kotlin.aws.runtime.tasks.createGraalJar
import com.kotlin.aws.runtime.tasks.createShadowJarGraal
import com.kotlin.aws.runtime.utils.kotlin
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get


class RuntimeKotlinGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.github.johnrengelman.shadow")
            pluginManager.apply("com.bmuschko.docker-remote-api")

            runtime = RuntimePluginExtension()

            val jar = createGraalJar()
            val shadow = createShadowJarGraal(jar)

            val generateAdapter = tasks.create("generateAdapter", GenerateAdapter::class.java)
            tasks.getByName("classes").dependsOn(generateAdapter)

            afterEvaluate {
                target.mySourceSets.apply {
                    this["main"].kotlin.srcDir(runtime.generationPathOrDefault(target))
                }
                ConfigureGraal.setupGraalVMTasks(target, shadow)
            }
        }
    }
}
