package com.example.productexplorer.data.repository

import com.example.productexplorer.data.local.ProductDao
import com.example.productexplorer.data.remote.ProductApi
import com.example.productexplorer.data.remote.ProductRemoteDataSource
import com.example.productexplorer.model.Product
import com.example.productexplorer.utils.NetworkBoundResource
import com.example.productexplorer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ProductRepository @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) {
    fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())

        // Fetch local products first
        val localProducts = dao.getAllProducts()
        if (localProducts.isNotEmpty()) {
            // Emit cached data
            emit(Resource.Success(data = localProducts))
        } else {
            emit(Resource.Loading(data = null))
        }

        // Try fetching data from remote
        try {
            val remoteProducts = api.getProducts()

            // Update local database after successful fetch
            dao.run {
                deleteAllProducts()
                insertProducts(remoteProducts)
            }

            // Emit remote data
            emit(Resource.Success(data = remoteProducts))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No Internet Connection",
                data = localProducts
            ))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Server Error",
                data = localProducts
            ))
        }
    }

    fun getProductDetails(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())

        // Fetch the product locally
        val localProduct = dao.getProductById(id)
        if (localProduct != null) {
            // Emit cached data if available
            emit(Resource.Success(data = localProduct))
        } else {
            emit(Resource.Loading(data = null))
        }

        // Try fetching details from remote
        try {
            val remoteProduct = api.getProductDetails(id)

            // Update local database after successful fetch
            dao.insertProduct(remoteProduct)

            // Emit remote data
            emit(Resource.Success(data = remoteProduct))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "No Internet Connection",
                data = localProduct
            ))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Server Error",
                data = localProduct
            ))
        }
    }
}
