package com.vml.tutorial.plantshop.core.presentation
expect
object DateTimeFormatter {
    fun format(timeInSec: Long, format: String): String
}