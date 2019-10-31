package com.example.statemachinetesting.state

import androidx.lifecycle.Lifecycle
import com.example.statemachinetesting.model.Repo

data class MainActivityState(
    val lifecycle: Lifecycle.State = Lifecycle.State.DESTROYED,
    val message: String = "MainState",
    val repos: List<Repo>? = null
)
