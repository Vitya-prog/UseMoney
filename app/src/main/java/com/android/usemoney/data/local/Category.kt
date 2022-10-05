package com.android.usemoney.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.usemoney.R
import java.util.*

@Entity(tableName="category")
data class Category(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val type:String = "Расходы",
    var value:Double = 0.0,
    val icon: String="R.drawable.cafe_icon",
    val color:String = ""
)
