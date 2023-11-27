package com.vml.tutorial.plantshop.plants.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(state: HomeScreenState, onEvent: (HomeScreenEvent) -> Unit) {
    Scaffold {
        Column {
            titleSection()
            searchSection()
            categorySection()
            offerSection()
            plantList()
        }
    }
}

@Composable
private fun titleSection() {

}

@Composable
private fun searchSection() {

}

@Composable
private fun categorySection() {

}

@Composable
private fun offerSection() {

}

@Composable
private fun plantList() {

}