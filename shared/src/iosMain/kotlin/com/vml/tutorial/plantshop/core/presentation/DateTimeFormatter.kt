package com.vml.tutorial.plantshop.core.presentation

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970


actual
object DateTimeFormatter {
    actual fun format(timeInSec: Long, format: String): String {
        // TODO: Use Yusuf version (fun formatDate(dateMillis: Long, dateFormat: String)) if it is available on this branch
        return NSDateFormatter().apply {
            dateFormat = format
            locale = NSLocale.currentLocale
        }.stringFromDate(NSDate.dateWithTimeIntervalSince1970(timeInSec.toDouble()))
    }
}