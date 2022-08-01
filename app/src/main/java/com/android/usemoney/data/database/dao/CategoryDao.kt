package com.android.usemoney.data.database.dao


import androidx.room.*
import com.android.usemoney.data.model.Category


@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE type = 'Расходы'")
    fun getCostCategories(): List<Category>
    @Query("SELECT * FROM category WHERE type = 'Доходы'")
    suspend fun getIncomeCategories(): List<Category>
    @Query("SELECT value FROM change WHERE name=:name")
    suspend fun getChangesList(name:String):List<Double>
    @Insert
    fun addCategory(category: Category)
    @Delete
    fun deleteCategory(category: Category)
    @Update
    fun updateCategory(category: Category)
}