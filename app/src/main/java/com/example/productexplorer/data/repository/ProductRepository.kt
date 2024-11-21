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

class ProductRepository @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao
) {
    fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())

        val localProducts = dao.getAllProducts()
        emit(Resource.Loading(data = localProducts))

        try {
            val remoteProducts = api.getProducts()
            dao.deleteAllProducts()
            dao.insertProducts(remoteProducts)
            emit(Resource.Success(remoteProducts))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Network Error",
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

        val localProduct = dao.getProductById(id)
        emit(Resource.Loading(data = localProduct))

        try {
            val remoteProduct = api.getProductDetails(id)
            dao.insertProduct(remoteProduct)
            emit(Resource.Success(remoteProduct))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Network Error",
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