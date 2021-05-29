package com.abproject.tsp_cart.model.database

import androidx.room.TypeConverter
import com.abproject.tsp_cart.model.dataclass.Product

class Converters {

    @TypeConverter
    fun fromString(
        productPicturesListString: String,
    ): List<String> {
        return productPicturesListString.split(",").map { it.trim() }
    }

    @TypeConverter
    fun toString(
        productPicturesList: List<String>,
    ): String {
        return productPicturesList.joinToString()
    }
}