package com.vml.tutorial.plantshop.core.utils

import java.text.SimpleDateFormat

actual fun formatDate(dateMillis: Long, dateFormat: String): String =
    SimpleDateFormat(dateFormat).format(dateMillis)
