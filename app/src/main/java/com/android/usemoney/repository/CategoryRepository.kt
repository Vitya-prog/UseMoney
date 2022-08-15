package com.android.usemoney.repository

import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.model.Category
import java.util.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class CategoryRepository @Inject constructor(
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

    suspend fun getCategory(id:UUID):Category = categoryDao.getCategory(id)

    suspend fun getIncomeSum():Double? = categoryDao.getIncomeSum()

    suspend fun getCostSum():Double? = categoryDao.getCostSum()

    suspend fun getChangeList(type:String):List<Double> = categoryDao.getChangesList(type)

    suspend fun getCostCategories():List<Category> = categoryDao.getCostCategories()

    suspend fun getIncomeCategories():List<Category> = categoryDao.getIncomeCategories()

    fun deleteCategory(category: Category) {
        executor.execute {
            categoryDao.deleteCategory(category)
        }
    }

}