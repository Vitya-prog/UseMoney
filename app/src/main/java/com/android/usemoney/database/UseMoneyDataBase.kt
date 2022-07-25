package com.android.usemoney.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.usemoney.database.dao.CategoryDao
import com.android.usemoney.database.dao.ChangeDao
import com.android.usemoney.database.dao.PlanDao
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.entities.ChangeEntity
import com.android.usemoney.entities.PlanEntity


@Database(version = 1, entities = [
    CategoryEntity::class,
    ChangeEntity::class,
    PlanEntity::class
],exportSchema = false)
@TypeConverters(UseMoneyTypeConverter :: class)
abstract class UseMoneyDataBase:RoomDatabase() {

    abstract fun changeDao(): ChangeDao
    abstract fun planDao():PlanDao
    abstract fun categoryDao():CategoryDao
}
