package com.example.productexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating

@Database(entities = [Product::class], version = 2, exportSchema = false)
@TypeConverters(ProductTypeConverters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        const val DATABASE_NAME = "product_db"
    }
}

// Type converters to handle complex objects in Room
object ProductTypeConverters {
    @TypeConverter
    fun fromRating(rating: Rating): String {
        return "${rating.rate},${rating.count}"
    }

    @TypeConverter
    fun toRating(ratingString: String): Rating {
        val parts = ratingString.split(",")
        return Rating(
            rate = parts[0].toDouble(),
            count = parts[1].toInt()
        )
    }
}