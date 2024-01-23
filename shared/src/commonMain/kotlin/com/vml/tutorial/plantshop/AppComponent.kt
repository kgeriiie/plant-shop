package com.vml.tutorial.plantshop

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.login.presentation.LoginComponent
import com.vml.tutorial.plantshop.main.presentation.DefaultMainComponent
import com.vml.tutorial.plantshop.main.presentation.MainComponent
import com.vml.tutorial.plantshop.register.presentation.RegisterComponent
import com.vml.tutorial.plantshop.splash.presentation.SplashComponent
import kotlinx.serialization.Serializable

interface AppComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onNavigateToMain()

    sealed class Child {
        data class SplashScreen(val component: SplashComponent) : Child()
        data class LoginScreen(val component: LoginComponent) : Child()
        data class MainScreen(val component: MainComponent) : Child()
        data class RegisterScreen(val component: RegisterComponent) : Child()
    }
}

class DefaultAppComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule,
) : AppComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    override val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SplashScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override fun onNavigateToMain() {
        navigation.replaceAll(Configuration.MainScreen)
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): AppComponent.Child {
        return when (config) {
            Configuration.MainScreen -> AppComponent.Child.MainScreen(DefaultMainComponent(context, appModule))
            Configuration.LoginScreen -> AppComponent.Child.LoginScreen(
                LoginComponent(
                    componentContext = context,
                    authRepository = appModule.authRepository,
                    onNavigateToMain = {
                        navigation.replaceAll(Configuration.MainScreen)
                    },
                    onNavigateRegister = {
                        navigation.pushNew(Configuration.RegisterScreen)
                    })
            )
            Configuration.SplashScreen -> AppComponent.Child.SplashScreen(SplashComponent(
                componentContext = context,
                appDataStore = appModule.dataStore,
                authRepository = appModule.authRepository,
                onNavigateToLogin = {
                    navigation.replaceAll(Configuration.LoginScreen)
                },
                onNavigateToMain = {
                    navigation.replaceAll(Configuration.MainScreen)
                }
            ))
            Configuration.RegisterScreen -> AppComponent.Child.RegisterScreen(
                RegisterComponent(
                    componentContext = context,
                    authRepository = appModule.authRepository,
                    registerUserRepository = appModule.registerUserRepository,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    onNavigateToMain = {
                        navigation.replaceAll(Configuration.MainScreen)
                    }
                )
            )
        }
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object SplashScreen: Configuration()
        @Serializable
        data object LoginScreen: Configuration()
        @Serializable
        data object MainScreen: Configuration()
        @Serializable
        data object RegisterScreen: Configuration()
    }
}