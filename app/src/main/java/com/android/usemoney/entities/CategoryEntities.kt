package com.android.usemoney.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CategoryEntities(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val color: String
)