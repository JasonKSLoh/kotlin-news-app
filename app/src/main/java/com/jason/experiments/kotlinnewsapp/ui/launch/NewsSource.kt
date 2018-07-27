package com.jason.experiments.kotlinnewsapp.ui.launch

import android.graphics.Bitmap

/**
 * NewsSource
 * Created by jason on 20/7/18.
 */

@Suppress("RemoveEmptyClassBody")
data class NewsSource(val title: String,
                      val logo: Bitmap,
                      val pluginPackage: String){
}