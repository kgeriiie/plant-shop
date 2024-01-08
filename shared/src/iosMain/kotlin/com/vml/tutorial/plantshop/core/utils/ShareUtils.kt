package com.vml.tutorial.plantshop.core.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareUtils() {
    actual fun shareContent(content: String) {
        val vc = UIActivityViewController(listOf(content), null)
        UIApplication.sharedApplication().keyWindow?.rootViewController?.presentViewController(vc, true, null)
    }
}