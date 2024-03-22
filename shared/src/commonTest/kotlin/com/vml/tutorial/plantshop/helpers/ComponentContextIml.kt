package com.vml.tutorial.plantshop.helpers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper

class ComponentContextIml(
    override val backHandler: BackHandler,
    override val instanceKeeper: InstanceKeeper,
    override val lifecycle: Lifecycle,
    override val stateKeeper: StateKeeper
) : ComponentContext
