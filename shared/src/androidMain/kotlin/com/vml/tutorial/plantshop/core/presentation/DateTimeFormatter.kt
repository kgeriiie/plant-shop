package com.vml.tutorial.plantshop.core.presentation

import java.text.SimpleDateFormat
import java.util.Date

actual
object DateTimeFormatter {
    actual fun format(timeInSec: Long, format: String): String {
        val df = SimpleDateFormat(format)
        return df.format(Date(timeInSec.times(1000)))
    }
}