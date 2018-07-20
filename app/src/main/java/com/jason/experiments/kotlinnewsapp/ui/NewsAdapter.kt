package com.jason.experiments.kotlinnewsapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.model.NewsResult
import com.jason.experiments.kotlinnewsapp.model.NytNewsResult
import com.jason.experiments.kotlinnewsapp.network.NetworkConsts
import com.jason.experiments.kotlinnewsapp.ui.web.WebActivity
import kotlinx.android.synthetic.main.item_news_result.view.*


/**
 * NewsAdapter
 * Created by jason on 7/3/18.
 */
@Suppress("MoveLambdaOutsideParentheses")
class NewsAdapter(entries: ArrayList<NytNewsResult>) : RecyclerView.Adapter<NewsViewHolder>() {
    public var newsResults = entries

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentEntry = newsResults[position]

        if (currentEntry.title == null || currentEntry.title.isNullOrBlank()) {
            val context = holder.view.context
            holder.tvTitle?.text = context?.getString(R.string.error_occurred_msg)
            holder.tvByline?.text = context?.getString(R.string.unable_to_load_msg)
            return
        }

        val media = currentEntry.multimedia
        if (media != null && media.isNotEmpty()) {
            val context = holder.view.context
            context?.let { ctx ->
                holder.ivThumbnail?.let { view ->
                    Glide.with(ctx).load(media[0].url).into(view)
                }
            }
        }
        holder.tvSection?.text = "[${currentEntry.section}]"
        if (!currentEntry.subsection.isNullOrBlank()) {
            holder.tvSubsection?.text = "[${currentEntry.subsection}]"
        } else {
            holder.tvSubsection?.text = ""
        }
        holder.tvTitle?.text = currentEntry.title
        holder.tvByline?.text = currentEntry.byline
        holder.tvAbstract?.text = currentEntry.abstract

        setCollapseExpandIcon(holder)
        holder.ivExpandCollapse?.setOnClickListener({
                                                        if (holder.tvAbstract?.visibility == View.VISIBLE) {
                                                            holder.tvAbstract.visibility = View.GONE
                                                            setCollapseExpandIcon(holder)
                                                        } else if (holder.tvAbstract?.visibility == View.GONE) {
                                                            holder.tvAbstract.visibility = View.VISIBLE
                                                            setCollapseExpandIcon(holder)
                                                        }
                                                    })

        holder.tvTitle?.setOnClickListener({
                                               val intent = Intent(holder.view.context, WebActivity::class.java)
                                               intent.putExtra(NetworkConsts.EXTRA_WV_URL, currentEntry.url)
                                               holder.view.context.startActivity(intent)
                                           })
    }

    override fun getItemCount(): Int {
        return newsResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_result, parent, false)
        return NewsViewHolder(view)
    }

    private fun setCollapseExpandIcon(holder: NewsViewHolder?) {
        if (holder?.tvAbstract?.visibility == View.VISIBLE) {
            holder.ivExpandCollapse?.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
        } else {
            holder?.ivExpandCollapse?.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
        }
    }

    public fun updateNewsResults(results: List<NytNewsResult>) {
        this.newsResults.clear()
        this.newsResults.addAll(results)
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
            val context = view.context
            ivThumbnail?.let {
                Glide.with(view.context).load(thumbUrl).into(it)
            }
        }
        tvSection?.text = "[${newsResult.section}]"
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
            ivExpandCollapse?.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
        } else {
            ivExpandCollapse?.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
        }
    }

}