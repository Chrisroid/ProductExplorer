package com.example.productexplorer.data.remote

import com.example.productexplorer.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRemoteDataSource @Inject constructor(
    private val productApi: ProductApi
) {
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = productApi.getProducts()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val response = productApi.getProductDetails(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}