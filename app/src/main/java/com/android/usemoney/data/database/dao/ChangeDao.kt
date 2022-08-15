package com.android.usemoney.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
import java.util.*

@Dao
interface ChangeDao {

    @Query("SELECT * FROM change")
    fun getChanges(): LiveData<List<Change>>
    @Query("SELECT * FROM category")
    suspend fun getIconCategories():List<Category>
    @Query("Select * FROM change WHERE id=:id")
    suspend fun getChange(id:UUID):Change
    @Query("SELECT * FROM change WHERE value=:value AND date=:date AND description=:description")
    suspend fun getChangeByParam(value:Double,date:Date,description:String):List<Category?>
    @Insert
    fun addChange(change: Change)
    @Delete
    fun deleteChange(change: Change)
    @Update
    fun updateChange(change: Change)
}