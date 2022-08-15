package com.android.usemoney.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="bill")
data class Bill(
    @PrimaryKey val id_bill:UUID,
    val name:String,
    var value:Double = 0.0
)