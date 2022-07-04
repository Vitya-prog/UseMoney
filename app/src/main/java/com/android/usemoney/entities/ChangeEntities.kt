package com.android.usemoney.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class ChangeEntities(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name:String,
    val value: Double,
    val category: CategoryEntities,
    val date:Date
)
