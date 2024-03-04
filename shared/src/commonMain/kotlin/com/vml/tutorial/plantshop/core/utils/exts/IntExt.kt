package com.vml.tutorial.plantshop.core.utils.exts

fun Int?.orZero(): Int {
    return this?: 0
}

fun Int?.orInvalid(): Int {
    return this?: -1
}

fun Int.toNonZeroString(): String {
    return toString().takeUnless { it == "0" }.orEmpty()
}
