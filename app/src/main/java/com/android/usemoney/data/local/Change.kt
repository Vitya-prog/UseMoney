package com.android.usemoney.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName="change")
data class Change(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    var name:String,
    val value: Double,
    var icon: Int,
    var color:String,
    val date:Date,
    var type:String,
    val description:String = "",
    val appcode:String = "0",
    val currency:Double = 1.0
)
