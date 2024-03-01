package com.vml.tutorial.plantshop.core.utils.exts

fun Long?.orZero(): Long {
    return this ?: 0
}
