package com.android.usemoney.repository

import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.model.Category
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CategoryRepository@Inject constructor(
    private val categoryDao: CategoryDao,
    private val executor: ExecutorService
) {
    fun addCategory(category: Category){
        executor.execute {
            categoryDao.addCategory(category)
        }
    }
    fun updateCategory(category:Category){
        executor.execute {
            categoryDao.updateCategory(category)
        }
    }
    suspend fun getChangeList(type:String):List<Double>{
        return categoryDao.getChangesList(type)
    }
    fun getCostCategories():List<Category> = categoryDao.getCostCategories()
    suspend fun getIncomeCategories():List<Category> = categoryDao.getIncomeCategories()
}