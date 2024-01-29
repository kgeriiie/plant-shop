package com.vml.tutorial.plantshop.core.utils.exts

import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Query

suspend inline fun <reified T> CollectionReference.getCollection(): List<T> {
    return try {
        get().documents.map { it.data() }
    } catch (err: Exception) {
        emptyList<T>()
    }
}

suspend inline fun <reified T> Query.getCollection(): List<T> {
    return try {
        get().documents.map { it.data() }
    } catch (err: Exception) {
        emptyList<T>()
    }
}
