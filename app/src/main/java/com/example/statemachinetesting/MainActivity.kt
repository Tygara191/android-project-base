package com.example.statemachinetesting

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.statemachinetesting.base.BaseActivity
import com.example.statemachinetesting.state.AppState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(this)

        tvHelloWorld.setOnClickListener { app.dispatch(listRepos()) }
    }

    private fun listRepos() = githubService()
        .listRepos("octocat")
        .toObservable()
        .subscribeOn(Schedulers.io())
        .flatMap{repos -> Observable
            .fromIterable(repos)
            .reduce("", {acc, repo -> "$acc\n${repo.name}."})
            .toObservable()
            .map{str -> { state:AppState -> state.copy(
                mainActivity = state.mainActivity.copy(
                    repos = repos,
                    message = str
                )
            )}}
        }
        .onErrorReturn {err -> {state: AppState ->
            state.copy(
                mainActivity = state.mainActivity.copy(
                    message = "An error has occured:\n $err"
                )
            )
        }}
        .observeOn(AndroidSchedulers.mainThread())

    override fun hook(oState: Observable<AppState>) = CompositeDisposable(
        oState
            .distinctUntilChanged{ state -> state.mainActivity.message }
            .map{ state -> state.mainActivity.message }
            .subscribe{ message -> tvHelloWorld.text = message}
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun lifecycleChange() = app.dispatch { state -> state.copy(
        mainActivity = state.mainActivity.copy(
            lifecycle = lifecycle.currentState
        )
    )}
}
