package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.OrdersDao
import com.lasec.monitoreoapp.data.database.entities.OrdersEntity
import javax.inject.Inject

class OrdersRepository @Inject constructor(
    private val ordersDao: OrdersDao
) {
    suspend fun upsert(order: OrdersEntity): Long {
        return ordersDao.upsert(order)
    }
}