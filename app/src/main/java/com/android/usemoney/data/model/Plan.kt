package com.android.usemoney.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName="plan")
data class Plan(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name: String,
    val dateFirst: Date,
    val dateSecond: Date,
    var startValue: Double,
    val endValue: Double,
    val icon: Int,
    val color:String
    )
