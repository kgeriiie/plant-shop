package com.vml.tutorial.plantshop.core.utils

import android.content.Context
import android.content.Intent
actual class ShareUtils(private val context: Context) {
    actual fun shareContent(content: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(sendIntent, null))
    }
}