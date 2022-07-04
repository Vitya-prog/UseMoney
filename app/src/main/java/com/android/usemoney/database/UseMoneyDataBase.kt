package com.android.usemoney.database

import androidx.room.Database
import com.android.usemoney.database.dao.CategoryDao
import com.android.usemoney.database.dao.ChangeDao
import com.android.usemoney.database.dao.PlanDao
import com.android.usemoney.entities.CategoryEntities
import com.android.usemoney.entities.ChangeEntities
import com.android.usemoney.entities.PlanEntities


@Database(version = 1, entities = [
    CategoryEntities::class,
    ChangeEntities::class,
    PlanEntities::class
],exportSchema = false)
abstract class UseMoneyDataBase {

    abstract fun changeDao(): ChangeDao
    abstract fun planDao():PlanDao
    abstract fun categoryDao():CategoryDao
}
