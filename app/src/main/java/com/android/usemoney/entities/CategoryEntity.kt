package com.android.usemoney.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CategoryEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val type:String,
    var value:Double = 0.0,
    val icon: Int,
    val color:String

)
