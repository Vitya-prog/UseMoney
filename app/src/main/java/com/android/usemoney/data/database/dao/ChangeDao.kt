package com.android.usemoney.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Change
import java.util.*

@Dao
interface ChangeDao {

    @Query("SELECT id,name,(value*currency) as value,icon,color,date,type,description,appcode,currency FROM change")
    fun getChanges(): LiveData<List<Change>>
    @Query("SELECT * FROM category WHERE name<>'Другие'")
    fun getIconCategories():LiveData<List<Category>>
    @Query("Select * FROM change WHERE id=:id")
    suspend fun getChange(id:UUID):Change
    @Query("SELECT * FROM change WHERE appcode=:appcode")
    suspend fun getChangeByParam(appcode:String):List<Category?>
    @Insert
    fun addChange(change: Change)
    @Delete
    fun deleteChange(change: Change)
    @Update
    fun updateChange(change: Change)
}