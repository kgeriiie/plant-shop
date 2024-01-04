package com.vml.tutorial.plantshop.core.utils.exts

fun Boolean?.orFalse(): Boolean {
    return this?: false
}

fun Boolean?.orTrue(): Boolean {
    return this?: true
}