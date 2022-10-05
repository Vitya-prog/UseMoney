package com.android.usemoney.data.database.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.local.Category
import java.util.*


@Dao
interface CategoryDao {
    @Query("SELECT id,name,type,(SELECT DISTINCT sum(value*currency) FROM change WHERE change.name = category.name AND change.type ='Расходы' AND change.date BETWEEN :dateTo AND :dateFrom) as value,icon,color FROM category WHERE type = 'Расходы'")
    fun getCostCategories(dateTo:Long,dateFrom:Long): LiveData<List<Category>>
    @Query("SELECT id,name,type,(SELECT DISTINCT sum(value) FROM change WHERE change.name = category.name AND change.type ='Доходы') as value,icon,color FROM category WHERE type = 'Доходы'")
    fun getIncomeCategories(): LiveData<List<Category>>
    @Query("Select * FROM category WHERE id =:id")
    fun getCategory(id: UUID): LiveData<Category>
    @Query("UPDATE change SET currency = :currency")
    fun updateCurrency(currency: Double)
    @Query("SELECT SUM(value) FROM change WHERE type ='Доходы'")
    suspend fun getIncomeSum():Double?
    @Query("SELECT SUM(value) FROM change WHERE type ='Расходы'")
    suspend fun getCostSum():Double?
    @Insert
    fun addCategory(category: Category)
    @Delete
    fun deleteCategory(category: Category)
    @Update
    fun updateCategory(category: Category)
}

//1662446089286