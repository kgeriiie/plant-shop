package com.vml.tutorial.plantshop.core.utils.exts

fun Int?.orZero(): Int {
    return this?: 0
}