package com.justjoeking.dmotron.network

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


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
