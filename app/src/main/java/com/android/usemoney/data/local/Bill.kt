package com.android.usemoney.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="bill", indices = [Index(value = arrayOf("name"), unique = true)])
data class Bill(
    @PrimaryKey val id_bill:UUID,
    val name:String,
    var value:Double = 0.0
)