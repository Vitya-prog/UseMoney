package com.android.usemoney.ui.history

import androidx.lifecycle.ViewModel
import com.android.usemoney.entities.ChangeEntity
import com.android.usemoney.repository.UseMoneyRepository

class HistoryViewModel : ViewModel() {
private val useMoneyRepository = UseMoneyRepository.get()
    val historyListLiveData = useMoneyRepository.getChanges()

    fun addChange(history: ChangeEntity){
        useMoneyRepository.addChanges(history)
    }
    fun deleteChange(history: ChangeEntity){
        useMoneyRepository.deleteChanges(history)
    }
}