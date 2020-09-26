package com.kotlin.aws.js.runtime.dsl

@RuntimeDSLTag
open class RuntimePluginExtension {

    var handler: String? = null
    var outputDir: String? = null

    fun getClassAndFunction(): Pair<String, String> {
        val handlerArray = handler?.split("::")
        require(handlerArray != null && handlerArray.size == 2) {
            "Kotlin JS Runtime requires correct `handler`." +
                    " The field should be set via `runtime` extension`."
        }
        val (klass, function) = handlerArray
        return klass to function
    }
}
