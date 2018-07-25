package com.jason.experiments.kotlinnewsapp.ui.news

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.network.NetworkConsts
import com.jason.experiments.kotlinnewsapp.ui.web.WebActivity
import com.net.learning.kotlinnewspluginlib.NewsResult
import kotlinx.android.synthetic.main.item_news_result.view.*

/**
 * NewsAdapter
 * Created by jason on 24/7/18.
 */
@Suppress("MoveLambdaOutsideParentheses")
class NewsAdapter(entries: ArrayList<NewsResult>) : RecyclerView.Adapter<NewsViewHolder>() {
    private var newsResults = entries

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentEntry = newsResults[position]

        val context = holder.view.context
        holder.tvTitle?.text = context?.getString(R.string.error_occurred_msg)
        holder.tvByline?.text = context?.getString(R.string.unable_to_load_msg)

        holder.setupViewholderUi(currentEntry)
    }

    override fun getItemCount(): Int {
        return newsResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_result, parent, false)
        return NewsViewHolder(view)
    }


    public fun setNewsResults(results: ArrayList<NewsResult>) {
        this.newsResults = results
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val view = itemView

    val tvSection: TextView? = itemView.tv_section
    val tvSubsection: TextView? = itemView.tv_subsection
    val tvTitle: TextView? = itemView.tv_title
    val tvByline: TextView? = itemView.tv_byline
    val tvAbstract: TextView? = itemView.tv_abstract
    val ivThumbnail: ImageView? = itemView.iv_thumbnail
    val ivExpandCollapse: ImageView? = itemView.iv_expand_collapse

    @Suppress("MoveLambdaOutsideParentheses")
    @SuppressLint("SetTextI18n")
    fun setupViewholderUi(newsResult: NewsResult) {
        if (newsResult.title.isBlank()) {
            val context = view.context
            tvTitle?.text = context?.getString(R.string.error_occurred_msg)
            tvByline?.text = context?.getString(R.string.unable_to_load_msg)
            return
        }

        val thumbUrl = newsResult.thumbUrl
        if (thumbUrl.isNotEmpty()) {
            ivThumbnail?.let {
                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.drawable.ic_loading_placeholder_24dp)
                Glide.with(view.context)
                        .load(thumbUrl)
                        .apply(requestOptions)
                        .into(it)
            }
        }
        tvSection?.let {
            if(newsResult.section.isNotBlank()){
                tvSection.text = "[${newsResult.section}]"
            }
        }
        if (!newsResult.subsection.isBlank()) {
            tvSubsection?.text = "[${newsResult.subsection}]"
        } else {
            tvSubsection?.text = ""
        }
        tvTitle?.text = newsResult.title
        tvByline?.text = newsResult.byline
        tvAbstract?.text = newsResult.abstract

        toggleCollapseExpandIcon()
        ivExpandCollapse?.setOnClickListener({
                                                 if (tvAbstract?.visibility == View.VISIBLE) {
                                                     tvAbstract.visibility = View.GONE
                                                     toggleCollapseExpandIcon()
                                                 } else if (tvAbstract?.visibility == View.GONE) {
                                                     tvAbstract.visibility = View.VISIBLE
                                                     toggleCollapseExpandIcon()
                                                 }
                                             })

        tvTitle?.setOnClickListener({
                                        val intent = Intent(view.context, WebActivity::class.java)
                                        intent.putExtra(NetworkConsts.EXTRA_WV_URL, newsResult.articleUrl)
                                        view.context.startActivity(intent)
                                    })


    }

    private fun toggleCollapseExpandIcon() {
        if (tvAbstract?.visibility == View.VISIBLE) {
            ivExpandCollapse?.setImageResource(R.drawable.ic_up_arrow_news_24dp)
        } else {
            ivExpandCollapse?.setImageResource(R.drawable.ic_down_arrow_news_24dp)
        }
    }

}