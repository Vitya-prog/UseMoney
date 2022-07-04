package com.android.usemoney.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.entities.PlanEntities

@Dao
interface PlanDao {

    @Query("SELECT * FROM PlanEntities")
    fun getPlan(): LiveData<List<PlanEntities>>
    @Insert
    fun addPlan(plan:PlanEntities)
    @Delete
    fun deletePlan(plan:PlanEntities)
    @Update
    fun updatePlan(plan: PlanEntities)
}