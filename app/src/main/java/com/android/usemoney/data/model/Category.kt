package com.android.usemoney.data.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="category")
data class Category(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val type:String,
    var value:Double = 0.0,
    val icon: Int,
    val color:String
)
