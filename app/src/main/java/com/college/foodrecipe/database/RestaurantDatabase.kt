package com.college.foodrecipe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.foodrecipe.database.OrderDao

@Database(entities = [RestaurantEntity::class, OrderEntity::class], version = 1, exportSchema = false)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    abstract fun orderDao(): OrderDao

}
