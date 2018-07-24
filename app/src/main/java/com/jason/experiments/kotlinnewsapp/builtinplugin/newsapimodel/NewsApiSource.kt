package com.jason.experiments.kotlinnewsapp.builtinplugin.newsapimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * NewsApiSource
 * Created by jason on 24/7/18.
 */
class NewsApiSource {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}