package com.android.usemoney.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.usemoney.R
import java.util.*

@Entity(tableName="category")
data class Category(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var name: String = "Example",
    val type:String = "Расходы",
    var value:Double = 0.0,
    var icon: String="cafe_icon",
    var color:String = "#89CFF0"
)
