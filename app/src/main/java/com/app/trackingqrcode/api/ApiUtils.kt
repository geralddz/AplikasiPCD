package com.app.trackingqrcode.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtils {
//    val API_URL = "http://192.168.92.204/api/public/api/"
    val API_URL = "http://10.14.130.94/pcd/api/"

    fun getApiClientInstance() : Retrofit{
        val interceptor =  HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getUserService(): ApiInterface {
        return getApiClientInstance().create(ApiInterface::class.java)
    }
}