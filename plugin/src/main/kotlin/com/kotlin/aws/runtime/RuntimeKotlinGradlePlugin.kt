@file:Suppress("unused")

package com.kotlin.aws.runtime

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.kotlin.aws.runtime.tasks.ConfigureGraal
import com.kotlin.aws.runtime.tasks.GenerateAdapter
import com.kotlin.aws.runtime.utils.Groups
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.get


class RuntimeKotlinGradlePlugin : Plugin<Project> {
    @ExperimentalUnsignedTypes
    override fun apply(target: Project) {
        target.pluginManager.apply("com.github.johnrengelman.shadow")
        target.pluginManager.apply("com.bmuschko.docker-remote-api")

        val jar = target.tasks.create("graalJar", Jar::class.java) {
            it.group = Groups.build

            it.manifest { manifest ->
                manifest.attributes(
                    mapOf("Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt")
                )
            }
        }

        val shadow = target.tasks.create("shadowJarGraal", ShadowJar::class.java) {
            it.group = Groups.shadow
            it.archiveClassifier.set("graal")
            it.archiveVersion.set("")
            it.from(target.mySourceSets["main"].output)
            it.configurations.add(target.configurations["compileClasspath"])
            it.configurations.add(target.configurations["runtimeClasspath"])
            it.manifest.inheritFrom(jar.manifest)
        }

        target.afterEvaluate {
            //FIXME
            // for some reason if we add generated sources via that command
            // sources are ignored during compilation
            target.mySourceSets.apply {
                this["main"].java.srcDir(runtime.generationPath!!)
            }
        }

        val generateAdapter = target.tasks.create("generateAdapter", GenerateAdapter::class.java)

        //TODO disabled for now, since generated sources are ignored during compilation
//        target.tasks["classes"].dependsOn(generateAdapter)

        ConfigureGraal.apply(target, shadow)
    }
}
