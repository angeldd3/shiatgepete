package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Users

@Entity(tableName = "users_table")
data class UsersEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "full_name") val fullName: String
)

fun Users.toDatabase(): UsersEntity {
    return UsersEntity(
        id = this.id,
        fullName = this.fullName
    )
}