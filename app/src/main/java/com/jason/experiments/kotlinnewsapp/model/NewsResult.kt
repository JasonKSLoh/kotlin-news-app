package com.jason.experiments.kotlinnewsapp.model

/**
 * NewsResult
 * Created by jason on 20/7/18.
 */
data class NewsResult(val title: String,
                      val abstract: String,
                      val byline: String,
                      val section: String,
                      val subsection: String,
                      val articleUrl: String,
                      val thumbUrl: String) {
}