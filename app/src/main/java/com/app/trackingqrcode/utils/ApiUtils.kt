package com.app.trackingqrcode.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtils {

    companion object {
        const val SOCKET_URL = "http://10.113.145.76:6001"
    }
    private val API_URL = "http://10.113.145.76/api/public/api/"

    private fun getApiClientInstance() : Retrofit{
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