package com.example.my.thenewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.my.thenewsapp.R
import com.example.my.thenewsapp.Utils
import com.example.my.thenewsapp.db.SavedArticle
import com.example.my.thenewsapp.db.Source
import com.example.my.thenewsapp.mvvm.NewsDatabase
import com.example.my.thenewsapp.mvvm.NewsRepo
import com.example.my.thenewsapp.mvvm.NewsViewModel
import com.example.my.thenewsapp.mvvm.NewsViewModelFac
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentArticle : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var args: FragmentArgument

    private var stringCheck = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setTitle("Article View")

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)

        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val textTitle: TextView = view.findViewById(R.id.tvTitle)
        val tSource: TextView = view.findViewById(R.id.tvSource)
        val tDescription: TextView = view.findViewById(R.id.tvDescription)
        val tPubsLisHedAt: TextView = view.findViewById(R.id.tvPublishedAt)
        val imageView: ImageView = view.findViewById(R.id.articleImage)
        val source = Source(args.article.source!!.id, args.article.source!!.name)
        textTitle.setText(args.article.title)
        tSource.text = source.name
        tDescription.setText(args.article.description)
        tPubsLisHedAt.text = Utils.DateFormat(args.article.publishedAt)

        Glide.with(requireActivity()).load(args.article.urlToImage).into(imageView)
        viewModel.getSavedNews.observe(viewLifecycleOwner, Observer {
            for (i in it) {
                if (args.article.title == i.title) {
                    stringCheck = i.title
                }
            }
        })

        fab.setOnClickListener {
            if (args.article.title == stringCheck) {
                Log.e("fragArg", "exists")
            } else {
                viewModel.insertArticle(
                    SavedArticle(
                        0, args.article.description!!,
                        args.article.publishedAt!!, source,
                        args.article.title!!, args.article.url!!,
                        args.article.urlToImage!!
                    )
                )

                Log.e("fragArg", "saved")
                view.findNavController().navigate(R.id.action_fragmentArticle_to_fragmentSavedNews)
            }
        }
    }

}