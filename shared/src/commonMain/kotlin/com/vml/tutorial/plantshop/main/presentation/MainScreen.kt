package com.vml.tutorial.plantshop.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.vml.tutorial.plantshop.basket.presentation.BasketScreen
import com.vml.tutorial.plantshop.favourites.presentation.FavouritesScreen
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailScreen
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreen

@Composable
fun MainScreen(
    component: MainComponent
) {
    val childStack by component.childStack.subscribeAsState()
    val navBarVisible  by component.navigationBarVisible.subscribeAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Children(
            stack = childStack
        ) { child ->
            when(val instance = child.instance) {
                is MainComponent.Child.BasketScreen -> BasketScreen()
                is MainComponent.Child.FavouritesScreen -> FavouritesScreen()
                is MainComponent.Child.HomeScreen -> {
                    val homeState by instance.component.state.collectAsState()
                    HomeScreen(homeState) { event ->
                        instance.component.onEvent(event)
                    }
                }
                is MainComponent.Child.PlantDetailScreen -> {
                    val plantDetailState by instance.component.state.collectAsState()
                    PlantDetailScreen(plantDetailState) { event ->
                        instance.component.onEvent(event)
                    }
                }
            }
        }

        if (navBarVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    .padding(horizontal = 30.dp)
                    .clickable { /* Do nothing */ },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        component.navigateTo(MainComponent.Configuration.HomeScreen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        component.navigateTo(MainComponent.Configuration.FavouritesScreen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        component.navigateTo(MainComponent.Configuration.BasketScreen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ShoppingBasket,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}