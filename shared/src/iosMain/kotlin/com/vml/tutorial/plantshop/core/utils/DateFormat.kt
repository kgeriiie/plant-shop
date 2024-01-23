package com.vml.tutorial.plantshop.core.utils

import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun formatDate(dateMillis: Long, dateFormat: String): String {
    val components = NSDateComponents().apply {
        year = 1970
        month = 1
        day = 1
        second = (dateMillis / 1000)
    }
    val dateFormatter = NSDateFormatter().apply {
        this.dateFormat = dateFormat
        locale = NSLocale.currentLocale
    }
    val date = NSCalendar.currentCalendar.dateFromComponents(components)

    return dateFormatter.stringFromDate(date ?: NSDate())
}
