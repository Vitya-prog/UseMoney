package com.android.usemoney.data.database.dao


import androidx.room.*
import com.android.usemoney.data.model.Category
import java.util.*


@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE type = 'Расходы'")
    suspend fun getCostCategories(): List<Category>
    @Query("SELECT * FROM category WHERE type = 'Доходы'")
    suspend fun getIncomeCategories(): List<Category>
    @Query("SELECT value FROM change WHERE name=:name")
    suspend fun getChangesList(name:String):List<Double>
    @Query("Select * FROM category WHERE id =:id")
    suspend fun getCategory(id: UUID): Category
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