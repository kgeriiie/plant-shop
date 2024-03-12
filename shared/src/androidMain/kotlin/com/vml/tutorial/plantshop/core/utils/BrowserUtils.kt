package com.vml.tutorial.plantshop.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class BrowserUtils(private val context: Context) {
    actual fun browse(url: String) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(browserIntent)
    }
}
