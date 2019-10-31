package com.example.statemachinetesting.state

import androidx.lifecycle.Lifecycle

/**
 *SecondActivityState
 *
 *@author Vesko Nikolov /vnikolov@pkdevs.com/
 *@since 28.10.2019 г.
 */
class SecondActivityState {
    val lifecycle: Lifecycle.State = Lifecycle.State.DESTROYED
    val message: String = "MainState"
}