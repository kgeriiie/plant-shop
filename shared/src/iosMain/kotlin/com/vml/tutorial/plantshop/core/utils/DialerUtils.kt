package com.vml.tutorial.plantshop.core.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class DialerUtils() {
    actual fun dialNumber(number: String) {
        val url = NSURL.URLWithString("tel://($number)")
        url?.let { UIApplication.sharedApplication.openURL(it) }
    }
}
