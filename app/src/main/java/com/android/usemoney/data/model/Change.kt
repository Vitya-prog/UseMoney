package com.android.usemoney.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName="change")
data class Change(
    @PrimaryKey val id_change:UUID =UUID.randomUUID(),
    val name:String,
    val value: Double,
    val icon: Int,
    val color:String,
    val date:Date,
    val type:String
)
