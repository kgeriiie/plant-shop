package com.vml.tutorial.plantshop.core.data

import android.content.Context
import dev.icerock.moko.resources.FileResource

actual class FileReader(
    private val context: Context
) {
    actual fun loadFile(resource: FileResource): String? {
        return resource.readText(context)
    }
}