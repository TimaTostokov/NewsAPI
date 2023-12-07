package com.example.my.thenewsapp.mvvm

import androidx.lifecycle.LiveData
import com.example.my.thenewsapp.db.SavedArticle
import com.example.my.thenewsapp.service.RetrofitInstance

class NewsRepo(private val newsDao: NewsDao? = null) {

    fun getAllSavedNews(): LiveData<List<SavedArticle>> {
        return newsDao!!.getAllNews()
    }

    fun getNewsById(): LiveData<SavedArticle> {
        return newsDao!!.getNewsById()
    }

    suspend fun getBreakingNews(code: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(code, pageNumber)


    suspend fun getCategoryNews(code: String) =
        RetrofitInstance.api.getByCategory(code)

    fun deleteAll() {
        newsDao!!.deleteAll()
    }

    suspend fun insertNews(savedArticle: SavedArticle) {
        newsDao!!.insertNews(savedArticle)
    }

}