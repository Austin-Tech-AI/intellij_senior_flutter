package com.austintech.intellijseniorflutter.network

import okhttp3.OkHttpClient

object HttpClient {
    val client = OkHttpClient().newBuilder().readTimeout(60, java.util.concurrent.TimeUnit.SECONDS).build()
}