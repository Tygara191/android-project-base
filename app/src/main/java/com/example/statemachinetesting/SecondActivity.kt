package com.example.statemachinetesting

import android.util.Log
import com.example.statemachinetesting.base.BaseActivity
import com.example.statemachinetesting.state.AppState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 *SecondActivity
 *
 *@author Vesko Nikolov /vnikolov@pkdevs.com/
 *@since 28.10.2019 г.
 */
class SecondActivity : BaseActivity() {
    override fun hook(oState: Observable<AppState>) = CompositeDisposable()
}