package com.kotlin.aws.runtime

import java.io.File

@DslMarker
annotation class RuntimeDSLTag

open class RuntimePluginExtension {
    var handler: String? = null
    var generationPath: File? = null
}

var runtime = RuntimePluginExtension()

@RuntimeDSLTag
fun runtime(configure: RuntimePluginExtension.() -> Unit) {
    runtime.configure()
}