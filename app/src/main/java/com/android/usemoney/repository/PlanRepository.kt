package com.android.usemoney.repository

import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.PlanDao
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Plan
import java.util.concurrent.Executors
import javax.inject.Inject

class PlanRepository @Inject constructor(
    private val planDao: PlanDao,
) {
    private val executor = Executors.newSingleThreadExecutor()
    fun getPlan(): LiveData<List<Plan>> = planDao.getPlan()
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
    fun getIconCategories(): LiveData<List<Category>> = planDao.getIconCategories()

    fun deletePlan(plan: Plan) {
        executor.execute {
            planDao.deletePlan(plan)
        }
    }
}