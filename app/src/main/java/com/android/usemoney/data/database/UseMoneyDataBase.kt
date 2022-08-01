package com.android.usemoney.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.database.dao.PlanDao
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
import com.android.usemoney.data.model.Plan


@Database(version = 1, entities = [
    Category::class,
    Change::class,
    Plan::class
],exportSchema = false)
@TypeConverters(UseMoneyTypeConverter :: class)
abstract class UseMoneyDataBase:RoomDatabase() {

    abstract fun changeDao(): ChangeDao
    abstract fun planDao(): PlanDao
    abstract fun categoryDao(): CategoryDao
}
