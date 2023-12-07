package com.example.my.thenewsapp.service

import com.example.my.thenewsapp.Utils.Companion.API_KEY
import com.example.my.thenewsapp.db.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ): Response<News>

    @GET("v2/everything")
    suspend fun getByCategory(
        @Query("q")
        category: String = "",
        @Query("apiKey")
        apiKey : String = API_KEY
    ): Response<News>

}