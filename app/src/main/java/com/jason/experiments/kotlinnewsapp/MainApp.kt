package com.jason.experiments.kotlinnewsapp

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins

/**
 * MainApp
 * Created by jason on 7/3/18.
 */
class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler({e -> e.printStackTrace()})
    }
}