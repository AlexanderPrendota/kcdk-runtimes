package com.kotlin.aws.runtime.dsl

import java.io.File
import java.io.Serializable

@DslMarker
annotation class RuntimeDSLTag

open class RuntimePluginExtension : Serializable {
    var handler: String? = null
    var generationPath: File? = null
    val config: RuntimeConfig? = null
}

internal var runtime = RuntimePluginExtension()

@RuntimeDSLTag
fun runtime(configure: RuntimePluginExtension.() -> Unit) {
    runtime.configure()
}