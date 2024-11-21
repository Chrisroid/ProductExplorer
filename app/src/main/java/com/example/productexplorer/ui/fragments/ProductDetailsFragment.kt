package com.example.productexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.productexplorer.R
import com.example.productexplorer.databinding.FragmentProductDetailsBinding
import com.example.productexplorer.model.Product
import com.example.productexplorer.ui.viewmodels.ProductViewModel
import com.example.productexplorer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get product ID from arguments
        val productId = arguments?.getInt("productId") ?: return

        // Observe product details
        lifecycleScope.launchWhenStarted {
            viewModel.productDetails.collect { resource ->
                when (resource) {
                    is Resource.Loading -> binding.progressBar.isVisible = true
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        resource.data?.let { product ->
                            bindProductDetails(product)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        // Fetch product details
        viewModel.fetchProductDetails(productId)
    }

    private fun bindProductDetails(product: Product) {
        with(binding) {
            productTitle.text = product.title
            productDescription.text = product.description
            productPrice.text = "$${product.price}"
            productRating.text = "Rating: ${product.rating.rate} (${product.rating.count} reviews)"

            Glide.with(requireContext())
                .load(product.image)
                .into(productImage)
        }
    }
}
