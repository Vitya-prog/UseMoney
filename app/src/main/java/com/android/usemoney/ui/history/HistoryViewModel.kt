package com.android.usemoney.ui.history

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.local.Change
import com.android.usemoney.repository.ChangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val changeRepository: ChangeRepository
) : ViewModel() {
    val historyListLiveData = changeRepository.getChanges()

    fun addChange(history: Change){
        changeRepository.addChanges(history)
    }
    fun deleteChange(history: Change){
        changeRepository.deleteChanges(history)
    }
}