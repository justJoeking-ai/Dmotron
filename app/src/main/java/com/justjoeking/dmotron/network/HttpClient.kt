package com.justjoeking.dmotron.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object HttpClient {

    val client: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val builder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            })
        builder.build()
    }
}
