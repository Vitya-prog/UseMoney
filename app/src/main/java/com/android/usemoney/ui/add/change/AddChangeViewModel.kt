package com.android.usemoney.ui.add.change

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Change
import com.android.usemoney.repository.ChangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddChangeViewModel@Inject constructor(
    private val changeRepository: ChangeRepository
): ViewModel() {
    fun addChange(change: Change){
        changeRepository.addChanges(change)
    }
    val iconCategories = changeRepository.getIconCategories()

suspend fun getChange(id: UUID):Change{
    return changeRepository.getChange(id)
}
     fun updateChange(change:Change) = changeRepository.updateChange(change)
}