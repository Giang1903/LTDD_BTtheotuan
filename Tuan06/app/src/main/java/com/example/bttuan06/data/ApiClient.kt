package com.example.bttuan06.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val productApi: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://mock.apidog.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}
