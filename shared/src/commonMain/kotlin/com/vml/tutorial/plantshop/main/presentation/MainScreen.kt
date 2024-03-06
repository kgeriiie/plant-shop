package com.vml.tutorial.plantshop.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.vml.tutorial.plantshop.basket.presentation.components.BasketScreen
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.presentation.favourites.components.FavouritesScreen
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailScreen
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components.PreferencesScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.components.EditAddressScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.components.EditProfileScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components.GetHelpScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.components.PaymentMethodScreen
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components.ProfileScreen

@Composable
fun MainScreen(
    component: MainComponent
) {
    val snackBarHostState: SnackbarHostState = remember{ SnackbarHostState() }
    val childStack by component.childStack.subscribeAsState()
    val state by component.state.collectAsState()
    val actions: DefaultMainComponent.Actions? by component.actions.collectAsState(null)

    when(actions) {
        is DefaultMainComponent.Actions.ShowMessageAction -> {
            val action = (actions as DefaultMainComponent.Actions.ShowMessageAction)
            val message = action.message.asString()
            LaunchedEffect(action.timeStamp) {
                snackBarHostState.showSnackbar(message = message)
            }
        }
        else -> Unit
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(
                stack = childStack,
                animation = stackAnimation(fade())
            ) { child ->
                when(val instance = child.instance) {
                    is MainComponent.MainChild.BasketScreen -> {
                        val basketState by instance.component.state.collectAsState()
                        BasketScreen(basketState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                    is MainComponent.MainChild.FavouritesScreen -> {
                        val favoriteState by instance.component.state.collectAsState()
                        FavouritesScreen(favoriteState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                    is MainComponent.MainChild.HomeScreen -> {
                        val homeState by instance.component.state.collectAsState()
                        HomeScreen(homeState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                    is MainComponent.MainChild.PlantDetailScreen -> {
                        val plantDetailState by instance.component.state.collectAsState()
                        PlantDetailScreen(plantDetailState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                    is MainComponent.MainChild.ProfileScreen -> {
                        val profileState by instance.component.state.collectAsState()
                        ProfileScreen(profileState) {event ->
                            instance.component.onEvent(event)
                        }
                    }

                    is MainComponent.MainChild.PreferencesScreen -> {
                        val preferencesState by instance.component.state.collectAsState()
                        PreferencesScreen(preferencesState) { event ->
                            instance.component.onEvent(event)
                        }
                    }

                    is MainComponent.MainChild.EditAddressScreen -> {
                        val editAddressState by instance.component.state.collectAsState()
                        EditAddressScreen(editAddressState) { event ->
                            instance.component.onEvent(event)
                        }
                    }

                    is MainComponent.MainChild.EditPersonalInfoScreen -> {
                        val editPersonalInfoState by instance.component.state.collectAsState()
                        EditProfileScreen(editPersonalInfoState) { event ->
                            instance.component.onEvent(event)
                        }
                    }

                    is MainComponent.MainChild.PaymentMethodScreen -> {
                        val paymentMethodState by instance.component.state.collectAsState()
                        PaymentMethodScreen(paymentMethodState) { event ->
                            instance.component.onEvent(event)
                        }
                    }

                    is MainComponent.MainChild.GetHelpScreen -> {
                        GetHelpScreen { event ->
                            instance.component.onEvent(event)
                        }
                    }
                }
            }

            if (state.bottomNavigationVisible) {
                MainNavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    MainNavigationItem(
                        active = childStack.active.instance is MainComponent.MainChild.HomeScreen,
                        defaultIcon = Icons.Outlined.Home,
                        selectedIcon = Icons.Rounded.Home,
                        onClick = { component.onEvent(MainScreenEvent.OnHomeTabClicked) }
                    )

                    MainNavigationItem(
                        active = childStack.active.instance is MainComponent.MainChild.FavouritesScreen,
                        defaultIcon = Icons.Outlined.FavoriteBorder,
                        selectedIcon = Icons.Rounded.Favorite,
                        onClick = { component.onEvent(MainScreenEvent.OnFavouriteTabClicked) }
                    )

                    MainNavigationItem(
                        active = childStack.active.instance is MainComponent.MainChild.BasketScreen,
                        defaultIcon = Icons.Outlined.ShoppingBasket,
                        selectedIcon = Icons.Rounded.ShoppingBasket,
                        onClick = { component.onEvent(MainScreenEvent.OnBasketTabClicked) }
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigationBar(
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
            .padding(horizontal = 30.dp)
            .clickable { /* Do nothing */ },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun MainNavigationItem(
    active: Boolean,
    defaultIcon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = if (active) selectedIcon else defaultIcon,
            contentDescription = null,
            tint = Color.White
        )
    }
}
