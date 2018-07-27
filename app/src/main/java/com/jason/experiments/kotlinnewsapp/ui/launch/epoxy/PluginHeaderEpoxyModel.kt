package com.jason.experiments.kotlinnewsapp.ui.launch.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.jason.experiments.kotlinnewsapp.R

/**
 * PluginHeaderEpoxyModel
 * Created by jason on 27/7/18.
 */
open class PluginHeaderEpoxyModel : EpoxyModelWithHolder<PluginHeaderEpoxyHolder>() {
    @EpoxyAttribute
    var numPlugins = 0

    override fun bind(holder: PluginHeaderEpoxyHolder) {
        holder.setNumPlugins(numPlugins)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_launch_header
    }

    override fun createNewHolder(): PluginHeaderEpoxyHolder {
        return PluginHeaderEpoxyHolder()
    }
}

class PluginHeaderEpoxyHolder: EpoxyHolder() {

    private var tvNumPlugins: TextView? = null

    override fun bindView(itemView: View?) {
        tvNumPlugins = itemView?.findViewById(R.id.tv_header_num_plugins)!!
    }

    fun setNumPlugins(numPlugins: Int){
        tvNumPlugins?.text = "$numPlugins"
    }
}