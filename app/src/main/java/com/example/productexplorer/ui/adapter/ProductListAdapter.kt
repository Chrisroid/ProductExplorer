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
        holder.productName.text = product.title
        holder.productPrice.text = "$${product.price}"
        holder.productRating.text = "⭐ ${product.rating.rate}"
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            onProductClicked(product)
        }
    }

    override fun getItemCount() = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
