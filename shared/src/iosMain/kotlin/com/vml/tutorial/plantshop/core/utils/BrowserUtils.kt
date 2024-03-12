package com.vml.tutorial.plantshop.core.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class BrowserUtils {
    actual fun browse(url: String) {
        NSURL.URLWithString(url)?.let { UIApplication.sharedApplication().openURL(it) }
    }
}
