package com.android.usemoney.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.usemoney.data.database.dao.BillDao
import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.database.dao.PlanDao
import com.android.usemoney.data.local.Bill
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Change
import com.android.usemoney.data.local.Plan


@Database(version = 1, entities = [
    Category::class,
    Change::class,
    Plan::class,
    Bill::class
],exportSchema = false)
@TypeConverters(UseMoneyTypeConverter :: class)
abstract class UseMoneyDataBase: RoomDatabase() {

    abstract fun changeDao(): ChangeDao
    abstract fun planDao(): PlanDao
    abstract fun categoryDao(): CategoryDao
    abstract fun billDao():BillDao

}
