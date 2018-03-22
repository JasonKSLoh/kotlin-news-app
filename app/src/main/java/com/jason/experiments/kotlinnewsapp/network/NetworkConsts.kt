package com.jason.experiments.kotlinnewsapp.network

import com.jason.experiments.kotlinnewsapp.BuildConfig

/**
 * NetworkConsts
 * Created by jason on 8/3/18.
 */
object NetworkConsts {
    val DEFAULT_TIMEOUT = 30L
    val EXTRA_WV_URL = BuildConfig.APPLICATION_ID + "extra_wv_url"
    val WV_URL_DEFAULT = "https://nytimes.com"
}