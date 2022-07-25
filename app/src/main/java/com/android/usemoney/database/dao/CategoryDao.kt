package com.android.usemoney.database.dao


import androidx.room.*
import com.android.usemoney.entities.CategoryEntity


@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity WHERE type = 'Расходы'")
    fun getCostCategories(): List<CategoryEntity>
    @Query("SELECT * FROM CategoryEntity WHERE type = 'Доходы'")
    suspend fun getIncomeCategories(): List<CategoryEntity>
    @Query("SELECT * FROM CategoryEntity")
    suspend fun getIconCategories():List<CategoryEntity>
    @Query("SELECT value FROM ChangeEntity WHERE name=:name")
    suspend fun getChangesList(name:String):List<Double>
    @Insert
    fun addCategory(category:CategoryEntity)
    @Delete
    fun deleteCategory(category:CategoryEntity)
    @Update
    fun updateCategory(category: CategoryEntity)
}