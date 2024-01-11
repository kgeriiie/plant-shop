package com.vml.tutorial.plantshop.core.utils.exts

import platform.Foundation.NSPredicate

actual fun String.isValidEmail(): Boolean {
    val emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
    val emailPred = NSPredicate.predicateWithFormat("SELF MATCHES %@", emailRegEx)

    return emailPred.evaluateWithObject(this)
}