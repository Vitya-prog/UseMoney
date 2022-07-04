package com.android.usemoney.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.entities.ChangeEntities

@Dao
interface ChangeDao {

    @Query("SELECT * FROM ChangeEntities")
    fun getChange(): LiveData<List<ChangeEntities>>
    @Insert
    fun addChange(change:ChangeEntities)
    @Delete
    fun deleteChange(change:ChangeEntities)
    @Update
    fun updateChange(change: ChangeEntities)
}