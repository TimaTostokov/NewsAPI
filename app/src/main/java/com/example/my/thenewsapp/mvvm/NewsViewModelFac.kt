package com.example.my.thenewsapp.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsViewModelFac(private val newsRepo: NewsRepo, private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}