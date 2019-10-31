package com.example.statemachinetesting.base

import androidx.appcompat.app.AppCompatActivity
import com.example.statemachinetesting.StateMachineApp
import com.example.statemachinetesting.state.AppState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {
    val app by lazy { application as StateMachineApp }

    private var disposable: CompositeDisposable? = null

    override fun onStart() {
        super.onStart()
        disposable = hook(app.oState)
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    open fun hook(oState: Observable<AppState>): CompositeDisposable? = null

    fun githubService() = app.gitHubService
}