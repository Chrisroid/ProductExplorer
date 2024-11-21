package com.example.productexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.productexplorer.R
import com.example.productexplorer.databinding.FragmentProductListBinding
import com.example.productexplorer.ui.adapter.ProductListAdapter
import com.example.productexplorer.ui.viewmodels.ProductViewModel
import com.example.productexplorer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private val viewModel: ProductViewModel by activityViewModels()

    private lateinit var adapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        setupRecyclerView()

        // Observe products from ViewModel
        observeProducts()

        // Refresh data on pull-to-refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchProducts()
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductListAdapter(emptyList()) { product ->
            viewModel.selectProduct(product)
            findNavController().navigate(R.id.action_productListFragment_to_productDetailsFragment)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }


    private fun observeProducts() {
        lifecycleScope.launchWhenStarted {
            viewModel.products.collect { resource ->
                binding.swipeRefreshLayout.isRefreshing = resource is Resource.Loading
                when (resource) {
                    is Resource.Loading -> {
                        showShimmerEffect()
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Success -> {
                        hideShimmerEffect()
                        binding.progressBar.isVisible = false
                        resource.data?.let { adapter.updateData(it) }
                    }
                    is Resource.Error -> {
                        hideShimmerEffect()
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            resource.message ?: "An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }
}
