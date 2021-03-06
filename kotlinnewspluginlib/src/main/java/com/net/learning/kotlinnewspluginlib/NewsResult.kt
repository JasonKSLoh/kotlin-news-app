package com.net.learning.kotlinnewspluginlib

import android.os.Parcel
import android.os.Parcelable

/**
 * NewsResult
 * Created by jason on 20/7/18.
 *
 * @param title The title or headline of the article
 * @param abstract The article's abstract or short text
 * @param byline The byline or authors
 * @param section Which section the article belongs to
 * @param subsection Which subsection, if any, the article belongs to
 * @param articleUrl The URL for the full article
 * @param thumbUrl A thumbnail URL for the article
 */
data class NewsResult(val title: String,
                      val abstract: String,
                      val byline: String,
                      val section: String,
                      val subsection: String,
                      val articleUrl: String,
                      val thumbUrl: String):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(abstract)
        parcel.writeString(byline)
        parcel.writeString(section)
        parcel.writeString(subsection)
        parcel.writeString(articleUrl)
        parcel.writeString(thumbUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsResult> {
        override fun createFromParcel(parcel: Parcel): NewsResult {
            return NewsResult(parcel)
        }

        override fun newArray(size: Int): Array<NewsResult?> {
            return arrayOfNulls(size)
        }
    }
}