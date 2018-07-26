package com.jason.experiments.kotlinnewsapp.builtinplugin.newsapimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * NewsApiResponse
 * Created by jason on 24/7/18.
 */
@Suppress("RemoveEmptyClassBody")
data class NewsApiResponse(@SerializedName("status")
                           @Expose
                           var status: String? = null,
                           @SerializedName("totalResults")
                           @Expose
                           var totalResults: Int? = null,
                           @SerializedName("articles")
                           @Expose
                           var articles: List<NewsApiArticle>? = null) {

}