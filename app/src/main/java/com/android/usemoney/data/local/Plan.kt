package com.android.usemoney.data.local



import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName="plan")
data class Plan(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name: String,
    val dateFirst: Date,
    val dateSecond: Date,
    val startValue: Double,
    val endValue: Double,
    val icon: Int,
    val color:String
    )
