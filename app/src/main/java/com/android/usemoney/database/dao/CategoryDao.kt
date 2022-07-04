package com.android.usemoney.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.entities.CategoryEntities


@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntities")
    fun getCategories(): LiveData<List<CategoryEntities>>
    @Insert
    fun addCategory(category:CategoryEntities)
    @Delete
    fun deleteCategory(category:CategoryEntities)
    @Update
    fun updateCategory(category: CategoryEntities)
}