package com.android.usemoney.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Plan

@Dao
interface PlanDao {

    @Query("SELECT id,name,dateFirst,dateSecond,(select sum(value*currency) from change where plan.name = change.name AND change.date >= plan.dateFirst) as startValue,endValue,icon,color FROM plan")
    fun getPlan(): LiveData<List<Plan>>
    @Query("SELECT * FROM category")
    fun getIconCategories(): LiveData<List<Category>>
    @Insert
    fun addPlan(plan: Plan)
    @Delete
    fun deletePlan(plan: Plan)
    @Update
    fun updatePlan(plan: Plan)
}