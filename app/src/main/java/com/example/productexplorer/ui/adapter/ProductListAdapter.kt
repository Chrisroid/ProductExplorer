package com.example.productexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productexplorer.R
import com.example.productexplorer.model.Product

class ProductListAdapter(
    private var products: List<Product>,
    private val onProductClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productRating: TextView = itemView.findViewById(R.id.productRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Bind the product details to the views
        holder.productName.text = product.title
        holder.productPrice.text = "$${product.price}"
        holder.productRating.text = "⭐ ${product.rating.rate}"

        // Load the product image using Glide
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.productImage)

        // Set up the click listener for the entire item view
        holder.itemView.setOnClickListener {
            onProductClicked(product) // Call the callback with the clicked product
        }
    }


    override fun getItemCount() = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
