package com.net.learning.nytplugin.nyt

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * NytNewsMultimedia
 * Created by jason on 7/3/18.
 */

class NytNewsMultimedia: Serializable {

    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("format")
    @Expose
    var format: String? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("subtype")
    @Expose
    var subtype: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("copyright")
    @Expose
    var copyright: String? = null

}