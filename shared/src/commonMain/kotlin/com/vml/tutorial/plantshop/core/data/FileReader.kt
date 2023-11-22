package com.vml.tutorial.plantshop.core.data

import dev.icerock.moko.resources.FileResource

expect class FileReader {
    fun loadFile(resource: FileResource): String?
}