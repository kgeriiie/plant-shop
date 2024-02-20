package com.vml.tutorial.plantshop.core.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun formatDate(dateMillis: Long, dateFormat: String): String {
    return NSDateFormatter().apply {
        this.dateFormat = dateFormat
        locale = NSLocale.currentLocale
    }.stringFromDate(NSDate.dateWithTimeIntervalSince1970(dateMillis.div(1000.0)))
}