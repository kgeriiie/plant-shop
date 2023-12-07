package com.vml.tutorial.plantshop.core.utils

actual object Logger {
    actual fun d(tag: String, text: String) {
        print("$tag :: $text")
    }
}
