package com.example.bttuan06.data

import retrofit2.http.GET

interface ProductApi {
    @GET("m1/890655-872447-default/v2/product")
    suspend fun getProduct(): Product
}
