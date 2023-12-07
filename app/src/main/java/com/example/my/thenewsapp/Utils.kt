package com.example.my.thenewsapp

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Utils {

    companion object {
        const val API_KEY = "INSERT YOU API KEY"
        const val BASE_URL = "https://newsapi.org"

        @SuppressLint("SimpleDateFormat")
        fun DateFormat(oldStringDate: String?): String? {
            val newDate: String?
            val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(getCountry()))
            newDate = try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldStringDate!!)
                dateFormat.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
                oldStringDate
            }

            return newDate
        }

        private fun getCountry(): String {
            val locale = Locale.getDefault()
            val country = java.lang.String.valueOf(locale.country)
            return country.lowercase(Locale.getDefault())
        }
    }

}