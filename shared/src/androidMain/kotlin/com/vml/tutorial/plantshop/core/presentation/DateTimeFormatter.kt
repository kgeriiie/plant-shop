package com.vml.tutorial.plantshop.core.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual
object DateTimeFormatter {
    actual fun format(timeInSec: Long, format: String): String {
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(Date(timeInSec.times(1000)))
    }
}