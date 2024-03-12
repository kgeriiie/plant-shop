package com.vml.tutorial.plantshop.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class DialerUtils(private val context: Context) {
    actual fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse("tel:$number"))
        context.startActivity(intent)
    }
}
