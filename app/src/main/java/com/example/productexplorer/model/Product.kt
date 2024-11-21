package com.example.productexplorer.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "price")
    val price: Double,

    @Json(name = "description")
    val description: String,

    @Json(name = "category")
    val category: String,

    @Json(name = "image")
    val image: String,

    @Embedded
    @Json(name = "rating")
    val rating: Rating
)

data class Rating(
    @Json(name = "rate")
    val rate: Double,

    @Json(name = "count")
    val count: Int
)