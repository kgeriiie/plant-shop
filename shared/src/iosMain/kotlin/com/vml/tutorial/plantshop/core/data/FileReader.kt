package com.vml.tutorial.plantshop.core.data

import dev.icerock.moko.resources.FileResource

actual class FileReader {
    actual fun loadFile(resource: FileResource): String? {
        return resource.readText()
    }
}