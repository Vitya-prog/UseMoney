package com.android.usemoney.repository

import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Change
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
    fun getIconCategories():LiveData<List<Category>> = changeDao.getIconCategories()
    suspend fun getChange(id: UUID):Change=changeDao.getChange(id)
    fun deleteChanges(change: Change){
        executor.execute {
            changeDao.deleteChange(change)
        }
    }
}