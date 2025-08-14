package com.lasec.monitoreoapp.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.OrdersEntity

@Dao
interface OrdersDao {

    @Query("SELECT * FROM orders_table ORDER BY created_at DESC")
    suspend fun getAllOrders(): List<OrdersEntity>

    @Upsert
    suspend fun upsert(order: OrdersEntity): Long

    @Query("DELETE FROM orders_table")
    suspend fun deleteAllOrders()
}
