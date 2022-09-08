package com.android.usemoney.repository

import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.local.Category
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

    fun updateCurrency(currency: Double){
        executor.execute {
            categoryDao.updateCurrency(currency)
        }
    }

    suspend fun getCategory(id:UUID):Category = categoryDao.getCategory(id)

    suspend fun getIncomeSum():Double? = categoryDao.getIncomeSum()

    suspend fun getCostSum():Double? = categoryDao.getCostSum()


    fun getCostCategories(dateTo: Long, dateFrom: Long): LiveData<List<Category>> = categoryDao.getCostCategories(dateTo, dateFrom)

    fun getIncomeCategories(): LiveData<List<Category>> = categoryDao.getIncomeCategories()

    fun deleteCategory(category: Category) {
        executor.execute {
            categoryDao.deleteCategory(category)
        }
    }

}