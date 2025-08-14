package com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.PlaceWorkOrderEntity

@Dao
interface PlaceWorkOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaceWorkOrders(entities: List<PlaceWorkOrderEntity>)

    @Query("""
        SELECT placeWorkOrderId
        FROM place_work_orders
        WHERE workOrderId = :workOrderId AND placeId = :placeId
        ORDER BY extractionSequence ASC
        LIMIT 1
    """)
    suspend fun getPlaceWorkOrderId(workOrderId: String?, placeId: Int): String?
}