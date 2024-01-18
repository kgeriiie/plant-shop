package com.vml.tutorial.plantshop.core.utils.exts

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSPredicate
import platform.Foundation.NSString
import platform.Foundation.create

@OptIn(BetaInteropApi::class)
actual fun String.isValidEmail(): Boolean {
    val emailRegEx: NSString = NSString.create(string = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
    val emailPred = NSPredicate.predicateWithFormat("SELF MATCHES %@", emailRegEx)

    return emailPred.evaluateWithObject(this)
}