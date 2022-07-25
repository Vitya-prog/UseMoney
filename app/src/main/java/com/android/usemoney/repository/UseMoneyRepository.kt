package com.android.usemoney.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.android.usemoney.R
import com.android.usemoney.database.UseMoneyDataBase
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.entities.ChangeEntity
import com.android.usemoney.entities.PlanEntity
import java.util.*
import java.util.concurrent.Executors

class UseMoneyRepository private constructor(context: Context){


private val database:UseMoneyDataBase = Room.databaseBuilder(
    context.applicationContext,
    UseMoneyDataBase::class.java,
"usemoney-database"
).build()

    private val changeDao = database.changeDao()
    private val planDao = database.planDao()
    private val categoryDao = database.categoryDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getChanges():LiveData<List<ChangeEntity>> = changeDao.getChanges()
    fun addChanges(change: ChangeEntity){
        executor.execute {
        changeDao.addChange(change)
}

}
    fun deleteChanges(change:ChangeEntity){
        changeDao.deleteChange(change)
    }

    suspend fun getPlan():List<PlanEntity> = planDao.getPlan()
    fun addPlan(plan: PlanEntity){
        executor.execute{
            planDao.addPlan(plan)
        }
    }
    fun updatePlan(plan:PlanEntity) {
        executor.execute{
            planDao.updatePlan(plan)
        }
    }
    suspend fun getStartValue(name:String): List<Double> {
        return planDao.getStartValue(name)
    }

    fun addCategory(category: CategoryEntity){
        executor.execute {
            categoryDao.addCategory(category)
        }
    }
    suspend fun getIconCategories():List<CategoryEntity>{
        return categoryDao.getIconCategories()
    }
    suspend fun getChangeList(type:String):List<Double>{
        return categoryDao.getChangesList(type)
    }
    fun getCostCategories():List<CategoryEntity> = categoryDao.getCostCategories()
    suspend fun getIncomeCategories():List<CategoryEntity> = categoryDao.getIncomeCategories()
    companion object{
        private var INSTANCE: UseMoneyRepository? = null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE= UseMoneyRepository(context)
            }
        }
        fun get():UseMoneyRepository {
            return INSTANCE ?: throw IllegalStateException("HistoryRepository не инициализирован!")
        }

    }

}