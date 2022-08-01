package com.android.usemoney.data.database.dao

import androidx.room.*
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Plan

@Dao
interface PlanDao {

    @Query("SELECT * FROM plan")
    suspend fun getPlan(): List<Plan>
    @Query("SELECT change.value FROM plan,change WHERE change.name = :name")
    suspend fun getStartValue(name: String):List<Double>
    @Query("SELECT * FROM category")
    suspend fun getIconCategories():List<Category>
    @Insert
    fun addPlan(plan: Plan)
    @Delete
    fun deletePlan(plan: Plan)
    @Update
    fun updatePlan(plan: Plan)
}