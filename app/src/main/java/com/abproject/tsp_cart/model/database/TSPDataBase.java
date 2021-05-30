package com.abproject.tsp_cart.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.abproject.tsp_cart.model.database.dao.ProductDao;
import com.abproject.tsp_cart.model.database.dao.UserDao;
import com.abproject.tsp_cart.model.dataclass.Product;
import com.abproject.tsp_cart.model.dataclass.User;

@Database(
        version = 1,
        entities = {Product.class, User.class},
        exportSchema = false
)
@TypeConverters(Converters.class)
abstract public class TSPDataBase extends RoomDatabase {

    public abstract ProductDao productDao();

    public abstract UserDao userDao();
}
