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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.my.thenewsapp.R
import com.example.my.thenewsapp.Utils
import com.example.my.thenewsapp.db.Article

class ArticleAdapter() : RecyclerView.Adapter<ArticleHolder>() {

    private var newsList = listOf<Article>()
    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newlist, parent, false)
        return ArticleHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = newsList[position]
        val requestOption = RequestOptions()

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).apply(requestOption)
                .listener(object : RequestListener<Drawable> {
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
            holder.tvSource.text = article.source!!.name
            holder.tvDescription.text = article.description
            holder.tvPublishedAt.text = Utils.DateFormat(article.publishedAt)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClicked(position, article)
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(articles: List<Article>) {
        this.newsList = articles
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filteredList(newFilteredList: List<Article>) {
        this.newsList = newFilteredList
        notifyDataSetChanged()
    }
}

class ArticleHolder(itemView: View) : ViewHolder(itemView) {
    val textTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvSource: TextView = itemView.findViewById(R.id.tvSource)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    val imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    val pb: ProgressBar = itemView.findViewById(R.id.pbImage)
}

interface ItemClickListener {
    fun onItemClicked(position: Int, article: Article)
}

