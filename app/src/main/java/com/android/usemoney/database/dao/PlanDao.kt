package com.android.usemoney.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.entities.PlanEntity

@Dao
interface PlanDao {

    @Query("SELECT * FROM PlanEntity")
    suspend fun getPlan(): List<PlanEntity>
    @Query("SELECT ChangeEntity.value FROM PlanEntity,ChangeEntity WHERE ChangeEntity.name = :name")
    suspend fun getStartValue(name: String):List<Double>
    @Insert
    fun addPlan(plan:PlanEntity)
    @Delete
    fun deletePlan(plan:PlanEntity)
    @Update
    fun updatePlan(plan: PlanEntity)
}