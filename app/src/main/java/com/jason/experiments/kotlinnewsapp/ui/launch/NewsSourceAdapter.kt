package com.jason.experiments.kotlinnewsapp.ui.launch

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jason.experiments.kotlinnewsapp.R
import kotlinx.android.synthetic.main.item_news_source.view.*

/**
 * NewsSourceAdapter
 * Created by jason on 20/7/18.
 */
class NewsSourceAdapter(private val newsSources: List<NewsSource>)
    : RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_source, parent, false)
        return NewsSourceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsSources.size
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        val newsSource = newsSources[position]
        holder.setupUi(newsSource)

    }


    class NewsSourceViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var ivLogo = itemView?.iv_newssource_logo
        var tvName = itemView?.tv_newssource_name

        fun setupUi(newsSource: NewsSource) {
            Log.d("+_", "In setup viewholder")
            itemView?.context?.let { ctx ->
                ivLogo?.let { iv ->
                    iv.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(ctx)
                            .load(newsSource.resourceId)
                            .into(iv)
                    Log.d("+_", "Set imageview: ${newsSource.resourceId}")
                }
            }
            tvName?.let {
                it.text = newsSource.title
                Log.d("+_", "Set title: ${newsSource.title}")
            }
        }
    }
}