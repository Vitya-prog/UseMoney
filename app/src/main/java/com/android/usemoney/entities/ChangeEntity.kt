package com.android.usemoney.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class ChangeEntity(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name:String,
    val value: Double,
    val icon: Int,
    val color:String,
    val date:Date,
    val type:String
)
