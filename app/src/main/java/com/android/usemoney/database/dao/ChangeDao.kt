package com.android.usemoney.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.entities.ChangeEntity

@Dao
interface ChangeDao {

    @Query("SELECT * FROM ChangeEntity")
    fun getChanges(): LiveData<List<ChangeEntity>>
    @Insert
    fun addChange(change:ChangeEntity)
    @Delete
    fun deleteChange(change:ChangeEntity)
    @Update
    fun updateChange(change: ChangeEntity)
}