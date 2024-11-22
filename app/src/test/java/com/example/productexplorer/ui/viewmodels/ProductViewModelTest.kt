package com.example.productexplorer.ui.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.productexplorer.data.local.ProductDao
import com.example.productexplorer.data.remote.ProductApi
import com.example.productexplorer.data.repository.ProductRepository
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.utils.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.time.Duration
import kotlin.time.Duration.Companion.seconds

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductViewModel
    @Mock
    private lateinit var mockDao: ProductDao

    @Mock
    private lateinit var mockApi: ProductApi

    private val sampleProducts = listOf(
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
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        mockDao = mockk(relaxed = true)
        mockApi = mockk(relaxed = true)

        repository = mockk(relaxed = true) // Mock the repository
        viewModel = ProductViewModel(repository)

        // Mock database behavior
        coEvery { mockDao.getAllProducts() } returns sampleProducts

        // Mock API behavior
        coEvery { mockApi.getProducts() } returns sampleProducts
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
//        clearInvocations(mockApi, mockDao)
    }



    @Test
    fun `selectProduct updates selectedProduct state`() = runTest {
        // Given
        val product = sampleProducts[0]

        // When
        viewModel.selectProduct(product)

        // Then
        viewModel.selectedProduct.test {
            assertEquals(product, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchProducts updates products state to success`() = runTest {
        // Given: The repository emits a successful resource with sample products
        val successResource = Resource.Success(sampleProducts)
        coEvery { repository.getProducts() } returns flowOf(successResource) // Mock the repository call

        // When: fetchProducts is called
        viewModel.fetchProducts()

        // Then: Verify the products state updates correctly
        viewModel.products.test {
            assertEquals(Resource.Loading<List<Product>>(), awaitItem()) // Initial state
            assertEquals(successResource, awaitItem()) // Emitted state from the repository
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchProducts updates products state to error`() = runTest {
        // Given: The repository emits an error resource
        val errorResource = Resource.Error<List<Product>>("Error fetching products")
        coEvery { repository.getProducts() } returns flowOf(errorResource)

        // When: fetchProducts is called
        viewModel.fetchProducts()

        // Then: Verify the products state updates correctly
        viewModel.products.test {
            assertEquals(Resource.Loading<List<Product>>(), awaitItem()) // Initial state
            assertEquals(errorResource, awaitItem()) // Emitted error state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchProducts updates products state to loading`() = runTest {
        // Given: The repository emits a loading resource
        val loadingResource = Resource.Loading<List<Product>>()
        coEvery { repository.getProducts() } returns flowOf(loadingResource)

        // When: fetchProducts is called
        viewModel.fetchProducts()

        // Then: Verify the products state updates correctly
        viewModel.products.test(timeout = 5.seconds) { // Extend timeout if needed
            assertEquals(Resource.Loading<List<Product>>(), awaitItem()) // Initial state
            cancelAndIgnoreRemainingEvents()
        }
    }
}
