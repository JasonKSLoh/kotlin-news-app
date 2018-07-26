package com.jason.experiments.kotlinnewsapp

import android.app.Application
import android.content.Context
import io.reactivex.plugins.RxJavaPlugins
import toothpick.Toothpick
import toothpick.config.Module

/**
 * MainApp
 * Created by jason on 7/3/18.
 */
@Suppress("unused")
class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { e -> e.printStackTrace()}

        val appScope = Toothpick.openScope(this)
        appScope.installModules(object : Module() {
            init {
                bind(Context::class.java).toInstance(this@MainApp)
            }
        })
        Toothpick.inject(this, appScope)
    }
}