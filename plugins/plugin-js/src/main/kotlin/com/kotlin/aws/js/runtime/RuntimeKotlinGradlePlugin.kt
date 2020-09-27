package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.tasks.*
import com.kotlin.aws.js.runtime.utils.createTask
import com.kotlin.aws.js.runtime.utils.runtime
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDceDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import java.io.File

@Suppress("unused")
class RuntimeKotlinGradlePlugin : Plugin<Project> {
    @ExperimentalDceDsl
    @ExperimentalDistributionDsl
    override fun apply(target: Project) {

        target.pluginManager.apply("org.jetbrains.kotlin.js")

        // set browser as a target
        val kotlinJsProjectExtension = target.extensions.getByName("kotlin") as KotlinJsProjectExtension
        kotlinJsProjectExtension.js {
            browser {
                useCommonJs()
                testTask {
                    enabled = false
                }
            }
            binaries.executable()
        }

        val customRuntimeMainClassTask = target.createTask(GenerateCustomRuntimeMainClass::class.java)
        val nodeJsRuntimeMainClassTask = target.createTask(GenerateNodeJsRuntimeHandlerWrapper::class.java)
        val webpackConfigTask = target.createTask(GenerateWebpackConfig::class.java)

        target.createTask(BuildCustomRuntimeLambda::class.java).apply {
            dependsOn(customRuntimeMainClassTask, webpackConfigTask, "assemble")
            target.tasks.findByName("compileKotlinJs")!!
                .mustRunAfter(customRuntimeMainClassTask, webpackConfigTask)
        }

        target.createTask(BuildNodeJsRuntimeLambda::class.java).apply {
            dependsOn(nodeJsRuntimeMainClassTask, webpackConfigTask, "assemble")
            target.tasks.findByName("compileKotlinJs")!!
                .mustRunAfter(nodeJsRuntimeMainClassTask, webpackConfigTask)
        }



        target.afterEvaluate {
            // tweak dce
            kotlinJsProjectExtension.js {
                browser {
                    val (_, function) = target.runtime.getClassAndFunction()
                    dceTask {
                        keep("${target.name}.${function}")
                    }
                }
                binaries.executable()
            }

            // tweak sourcesets-dir
            val sourceSetContainer = target.extensions.getByName("kotlin") as KotlinSourceSetContainer
            with(sourceSetContainer.sourceSets.getByName("main").kotlin) {
                this.setSrcDirs(this.srcDirs.plus(it.buildDir.absolutePath + "/kotlin-gen"))
            }

            // update distribution dir
            if (target.runtime.outputDir != null) {
                kotlinJsProjectExtension.js {
                    browser {
                        distribution {
                            directory = File(target.runtime.outputDir!!)
                        }
                    }
                }
            }
        }

    }
}
