package com.android.usemoney.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity()
data class PlanEntity(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name: String,
    val dateFirst: Date,
    val dateSecond: Date,
    var startValue: Double,
    val endValue: Double,
    val icon: Int,
    val color:String
    )
