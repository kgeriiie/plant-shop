package com.vml.tutorial.plantshop.core.utils

interface ShareHelper {
    fun shareContent(content: String)
}

expect class ShareHelperImpl: ShareHelper {
    override fun shareContent(content: String)
}