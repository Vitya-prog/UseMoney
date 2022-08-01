package com.android.usemoney.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change

@Dao
interface ChangeDao {

    @Query("SELECT * FROM change")
    fun getChanges(): LiveData<List<Change>>
    @Query("SELECT * FROM category")
    suspend fun getIconCategories():List<Category>
    @Insert
    fun addChange(change: Change)
    @Delete
    fun deleteChange(change: Change)
    @Update
    fun updateChange(change: Change)
}