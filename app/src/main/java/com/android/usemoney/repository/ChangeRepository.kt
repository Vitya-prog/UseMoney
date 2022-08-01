package com.android.usemoney.repository

import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
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
    suspend fun getIconCategories():List<Category>{
        return changeDao.getIconCategories()
    }
    fun deleteChanges(change: Change){
        changeDao.deleteChange(change)
    }
}