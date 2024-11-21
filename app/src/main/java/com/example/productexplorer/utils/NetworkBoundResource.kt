package com.example.productexplorer.utils

import com.example.productexplorer.utils.Resource.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


abstract class NetworkBoundResource<ResultType, RequestType> {
    fun asFlow(): Flow<Resource<ResultType>> = flow {
        // Emit loading state with cached data
        emit(Loading(data = loadFromDb()))

        // Decide whether to fetch from network
        val shouldFetch = shouldFetch(loadFromDb())

        if (shouldFetch) {
            // Attempt to fetch from network
            val apiResponse = createCall()

            if (apiResponse.isSuccess) {
                // Save network result to database
                apiResponse.getOrNull()?.let { networkResult ->
                    saveNetworkResult(networkResult)
                    // Emit success with fresh data
                    emit(Success(loadFromDb()))
                } ?: emit(Error("No data found", loadFromDb()))
            } else {
                // Emit error with cached data
                emit(
                    Error(
                    apiResponse.exceptionOrNull()?.message ?: "Unknown error",
                    loadFromDb()
                )
                )
            }
        } else {
            // Emit cached data if no fetch is needed
            emit(Success(loadFromDb()))
        }
    }

    // Save network response to local database
    protected abstract suspend fun saveNetworkResult(item: RequestType)

    // Determine if we need to fetch from network
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Load data from local database
    protected abstract suspend fun loadFromDb(): ResultType?

    // Create network call
    protected abstract suspend fun createCall(): Result<RequestType>
}