package com.abproject.tsp_cart.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.abproject.tsp_cart.model.dataclass.Product;

@Database(
        version = 1,
        entities = {Product.class},
        exportSchema = false
)
@TypeConverters(Converters.class)
abstract public class TSPDataBase extends RoomDatabase {

    public abstract ProductDao productDao();
}
