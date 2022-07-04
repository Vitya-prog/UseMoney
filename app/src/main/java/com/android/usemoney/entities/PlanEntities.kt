package com.android.usemoney.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class PlanEntities(
    @PrimaryKey val id:UUID =UUID.randomUUID(),
    val name: CategoryEntities,
    val dateFirst: Date,
    val dateSecond: Date,
    val value: Double,
    val color: String,
    val icon: String
    )
