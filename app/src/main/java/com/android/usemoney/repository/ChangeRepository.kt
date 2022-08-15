package com.android.usemoney.repository

import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class ChangeRepository @Inject constructor(
    private val changeDao: ChangeDao
) {
    private val executor = Executors.newSingleThreadExecutor()
    fun getChanges(): LiveData<List<Change>> = changeDao.getChanges()
    fun addChanges(change: Change) {
        executor.execute {
            changeDao.addChange(change)
        }
    }
    fun updateChange(change: Change) {
        executor.execute {
            changeDao.updateChange(change)
        }
    }
    suspend fun getIconCategories():List<Category>{
        return changeDao.getIconCategories()
    }
    suspend fun getChange(id: UUID):Change=changeDao.getChange(id)
    fun deleteChanges(change: Change){
        executor.execute {
            changeDao.deleteChange(change)
        }

    }
    suspend fun getChangeByParam(value:Double,date:Date,description:String):List<Category?> {
        return changeDao.getChangeByParam(value,date, description)
    }
}