package com.example.statemachinetesting

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.example.statemachinetesting.retrofit.GitHubService
import com.example.statemachinetesting.state.AppState
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class StateMachineApp : Application() {

    private val oActions: Subject<(AppState) -> AppState> = PublishSubject.create() // Actions observable
    val oState: ReplaySubject<AppState> = ReplaySubject.createWithSize<AppState>(1)

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val gitHubService: GitHubService by lazy {
        retrofit.create(GitHubService::class.java)
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()

        oActions.withLatestFrom(oState)
            .map{actionStatePair -> actionStatePair.first.invoke(actionStatePair.second)}
            .startWith(AppState())
            .distinctUntilChanged()
            .doOnNext{state -> Log.d("VesiTesting", "State Change: $state")}
            .subscribe(oState::onNext)
    }

    fun dispatch(action: (AppState) -> AppState){
        oActions.onNext(action)
    }

    fun dispatch(oAction: Observable<(AppState) -> AppState>){
        oAction
            .doOnNext { action -> oActions.onNext(action) }
            .doOnError { error ->
                Log.d("VesiTesting", "I errored out m8")
                throw error
            }
            .subscribe()
    }
}