package com.example.my.thenewsapp.mvvm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.my.thenewsapp.db.News
import com.example.my.thenewsapp.db.SavedArticle
import com.example.my.thenewsapp.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@Suppress("UNREACHABLE_CODE")
class NewsViewModel(private val newsRepo: NewsRepo, application: Application) :
    AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    private val pageNumber = 1
    val categoryNews: MutableLiveData<Resource<News>> = MutableLiveData()

    val getSavedNews = newsRepo.getAllSavedNews()

    init {
        getBreakingNews("us")
    }


    private fun getBreakingNews(code: String) = viewModelScope.launch {
        checkInternetAndBreakingNews(code)
    }

    private suspend fun checkInternetAndBreakingNews(code: String) {
        breakingNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getBreakingNews(code, pageNumber)
                breakingNews.postValue(handleNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("NO INTERNET CONNECTION"))
            }
        } catch (t: Throwable) {

            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("NETWORK FAILER"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleNewsResponse(response: Response<News>): Resource<News>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun getCategory(cat: String) = viewModelScope.launch {
        categoryNews.postValue(Resource.Loading())
        val response = newsRepo.getCategoryNews(cat)
        categoryNews.postValue(handleNewsResponse(response))
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return false
    }

    fun insertArticle(savedArt: SavedArticle) {
        insertNews(savedArt)
    }

    private fun insertNews(savedArt: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.insertNews(savedArt)
    }

    fun deleteAllArticles() {
        deleteAll()
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.deleteAll()
    }

}