package com.example.my.thenewsapp.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.my.thenewsapp.R
import com.example.my.thenewsapp.Utils
import com.example.my.thenewsapp.db.SavedArticle

class SavedArticleAdapter() : RecyclerView.Adapter<SavedHolder>() {

    private var newsList = listOf<SavedArticle>()
    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newlist, parent, false)
        return SavedHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: SavedHolder, position: Int) {
        val article = newsList[position]
        val requestOption = RequestOptions()

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).apply(requestOption).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility = View.GONE
                    return false
                }
            }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView)

            holder.textTitle.text = article.title
            holder.tvSource.text = article.source.name
            holder.tvDescription.text = article.description
            holder.tvPubsLisHedAt.text = Utils.DateFormat(article.publishedAt)
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(articles: List<SavedArticle>) {
        this.newsList = articles
        notifyDataSetChanged()
    }
}

class SavedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvSource: TextView = itemView.findViewById(R.id.tvSource)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    val tvPubsLisHedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    val imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    val pb: ProgressBar = itemView.findViewById(R.id.pbImage)
}