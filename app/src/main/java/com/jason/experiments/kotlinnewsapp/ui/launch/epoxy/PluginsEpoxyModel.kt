package com.jason.experiments.kotlinnewsapp.ui.launch.epoxy

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.ui.launch.NewsSource
import kotlinx.android.synthetic.main.item_news_source.view.*

/**
 * PluginsEpoxyModel
 * Created by jason on 27/7/18.
 */
open class PluginsEpoxyModel : EpoxyModelWithHolder<PluginsEpoxyViewHolder>() {
    @EpoxyAttribute
    lateinit var newsSource: NewsSource

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClickListener: View.OnClickListener

    override fun bind(holder: PluginsEpoxyViewHolder) {
        holder.setupUi(newsSource)
        holder.itemView?.setOnClickListener(onClickListener)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_news_source
    }

    override fun createNewHolder(): PluginsEpoxyViewHolder {
        return PluginsEpoxyViewHolder()
    }

}

open class PluginsEpoxyViewHolder : EpoxyHolder() {
    private var ivLogo: ImageView? = null
    private var tvName: TextView? = null
    private var context: Context? = null
    var itemView: View? = null

    override fun bindView(itemView: View?) {
        ivLogo = itemView?.iv_newssource_logo
        tvName = itemView?.tv_newssource_name
        this.itemView = itemView
        context = itemView?.context
    }

    fun setupUi(newsSource: NewsSource) {
        ivLogo?.let { iv ->
            context?.let { ctx ->
                iv.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(ctx)
                        .load(newsSource.logo)
                        .into(iv)
            }
            tvName?.let {
                it.text = newsSource.title
            }
        }

    }
}