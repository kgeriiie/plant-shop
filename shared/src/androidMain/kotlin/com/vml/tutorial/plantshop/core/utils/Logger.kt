package com.vml.tutorial.plantshop.core.utils

import android.util.Log

actual object Logger {
    actual fun d(tag: String, text: String) {
        Log.d(tag,text)
    }
}
