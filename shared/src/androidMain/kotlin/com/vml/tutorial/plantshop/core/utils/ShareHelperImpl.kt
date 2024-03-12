package com.vml.tutorial.plantshop.core.utils

import android.content.Context
import android.content.Intent

actual class ShareHelperImpl(private val context: Context): ShareHelper {
    actual override fun shareContent(content: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(sendIntent, null))
    }
}