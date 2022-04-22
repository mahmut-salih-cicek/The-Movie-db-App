package com.developersout.proje.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory




private val BASE_URL="https://api.themoviedb.org/"

object MovieApiServisUpComing {

    private val client = OkHttpClient
        .Builder()
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(MovieAPIUpComing::class.java)

    fun buildService(): MovieAPIUpComing {
        return retrofit
    }


}