package com.jason.experiments.kotlinnewsapp.builtinplugin.newsapimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jason.experiments.kotlinnewsapp.model.NewsResult


/**
 * NewsApiArticle
 * Created by jason on 24/7/18.
 */
class NewsApiArticle {
    @SerializedName("source")
    @Expose
    var newsApiSource: NewsApiSource? = null
    @SerializedName("author")
    @Expose
    var author: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("urlToImage")
    @Expose
    var urlToImage: String? = null
    @SerializedName("publishedAt")
    @Expose
    var publishedAt: String? = null

    fun toNewsResultStringArray(): Array<String>{
        val nrTitle = if (title == null) {
            ""
        } else {
            title!!
        }
        val nrSection = if (newsApiSource == null) {
            ""
        } else {
            if(newsApiSource!!.name == null){
                ""
            } else{
                newsApiSource!!.name!!
            }
        }
        val nrSubSection = ""
        val nrByline = if (author == null) {
            ""
        } else {
            author!!
        }
        val nrAbstract = if (description== null) {
            ""
        } else {
            description!!
        }
        val nrArticleUrl = if (url == null) {
            ""
        } else {
            url!!
        }
        val nrThumbUrl = if(urlToImage == null){
            ""
        } else{
            urlToImage!!
        }
        return arrayOf(nrTitle, nrAbstract, nrByline, nrSection, nrSubSection, nrArticleUrl, nrThumbUrl)

    }
    fun toNewsResult(): NewsResult {
        val stringArray = toNewsResultStringArray()
        return NewsResult(stringArray[0],
                          stringArray[1],
                          stringArray[2],
                          stringArray[3],
                          stringArray[4],
                          stringArray[5],
                          stringArray[6])
    }

}