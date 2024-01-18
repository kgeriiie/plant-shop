package com.vml.tutorial.plantshop.core.utils.exts

actual fun String.isValidEmail(): Boolean {
    return isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}