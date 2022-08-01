package com.android.usemoney.repository

import com.android.usemoney.data.database.dao.PlanDao
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Plan
import java.util.concurrent.Executors
import javax.inject.Inject

class PlanRepository @Inject constructor(
    private val planDao: PlanDao
) {
    private val executor = Executors.newSingleThreadExecutor()
    suspend fun getPlan():List<Plan> = planDao.getPlan()
    fun addPlan(plan: Plan) {
        executor.execute {
            planDao.addPlan(plan)
        }
    }
    fun updatePlan(plan: Plan) {
        executor.execute {
            planDao.updatePlan(plan)
        }
    }
    suspend fun getStartValue(name:String): List<Double> {
        return planDao.getStartValue(name)
    }
    suspend fun getIconCategories():List<Category>{
        return planDao.getIconCategories()
    }
}