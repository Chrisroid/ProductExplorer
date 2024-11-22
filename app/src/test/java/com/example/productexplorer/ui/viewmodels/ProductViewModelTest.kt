package com.example.productexplorer.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.productexplorer.data.repository.ProductRepository
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockRepository: ProductRepository

    private lateinit var viewModel: ProductViewModel

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
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProducts updates products state with repository data`() = runTest {
        val mockResource = Resource.Success(mockProducts)
        whenever(mockRepository.getProducts()).thenReturn(kotlinx.coroutines.flow.flowOf(mockResource))

        viewModel.fetchProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mockResource, viewModel.products.value)
    }

    @Test
    fun `selectProduct updates selected product state`() {
        val selectedProduct = mockProducts.first()

        viewModel.selectProduct(selectedProduct)

        assertEquals(selectedProduct, viewModel.selectedProduct.value)
    }
}