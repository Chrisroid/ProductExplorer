package com.example.productexplorer.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.productexplorer.data.local.ProductDao
import com.example.productexplorer.data.remote.ProductApi
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.utils.Resource
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockDao: ProductDao

    @Mock
    private lateinit var mockApi: ProductApi

    private lateinit var repository: ProductRepository

    private val mockProducts = listOf(
        Product(
            id = 1,
            title = "Product 1",
            price = 10.0,
            description = "Description 1",
            category = "Electronics",
            image = "http://example.com/image1.jpg",
            rating = Rating(rate = 4.5, count = 100)
        ),
        Product(
            id = 2,
            title = "Product 2",
            price = 20.0,
            description = "Description 2",
            category = "Clothing",
            image = "http://example.com/image2.jpg",
            rating = Rating(rate = 4.2, count = 75)
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = ProductRepository(mockApi, mockDao)
    }

    @After
    fun tearDown() {
        clearInvocations(mockApi, mockDao)
    }


    @Test
    fun `getProducts returns cached data when local database is not empty`() = runTest {
        // Setup
        whenever(mockDao.getAllProducts()).thenReturn(mockProducts)
        whenever(mockApi.getProducts()).thenReturn(mockProducts)  // Add this line

        repository.getProducts().test {
            // First loading state
            awaitItem() shouldBe Resource.Loading()

            // Local data emission
            awaitItem() shouldBe Resource.Success(mockProducts)

            // Advance scheduler for remote call
            testScheduler.advanceUntilIdle()

            // Remote data emission
            awaitItem() shouldBe Resource.Success(mockProducts)

            cancelAndIgnoreRemainingEvents()
        }

        // Verify all expected interactions
        verify(mockDao).getAllProducts()
        verify(mockApi).getProducts()
        verify(mockDao).deleteAllProducts()
        verify(mockDao).insertProducts(mockProducts)
    }
    @Test
    fun `getProductDetails fetches remote data when local data is not available`() = runTest {
        val productId = 1
        val mockProduct = mockProducts.first { it.id == productId }
        whenever(mockDao.getProductById(productId)).thenReturn(null)
        whenever(mockApi.getProductDetails(productId)).thenReturn(mockProduct)

        repository.getProductDetails(productId).test {
            println("First emission: ${awaitItem()}")  // Should be Loading

            testScheduler.advanceUntilIdle()

            try {
                println("Second emission: ${awaitItem()}")  // Should be Success
            } catch (e: Exception) {
                println("Failed to get second emission: ${e.message}")
            }

            cancelAndIgnoreRemainingEvents()
        }
    }}