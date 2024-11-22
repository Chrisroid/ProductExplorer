package com.example.productexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.productexplorer.R
import com.example.productexplorer.databinding.FragmentProductDetailsBinding
import com.example.productexplorer.model.Product
import com.example.productexplorer.ui.viewmodels.ProductViewModel
import com.example.productexplorer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedProduct.collect { product ->
                product?.let {
                    binding.productCategoryDetails.text = "Category: ${it.category}"
                    binding.productTitleDetails.text = it.title
                    binding.productPriceDetails.text = "$${it.price}"
                    binding.productDescriptionDetails.text = it.description
                    binding.ratingBar.rating = it.rating.rate.toFloat()
                    val ratingDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rating_drawable)
//                    binding.ratingBar.progressDrawable = ratingDrawable
                    binding.productReviewNumberDetails.text = "(${it.rating.count} Reviews)"
                    Glide.with(requireContext())
                        .load(it.image)
                        .into(binding.productImageDetails)
                }
            }
        }
    }

    // Fetch product details
}

