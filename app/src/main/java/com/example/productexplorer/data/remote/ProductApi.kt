package com.example.productexplorer.data.remote

import com.example.productexplorer.model.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductDetails(id: Int): Product
}