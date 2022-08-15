package com.android.usemoney.ui.add.change

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
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
    suspend fun getIconCategories():List<Category>{
        return changeRepository.getIconCategories()
    }

suspend fun getChange(id: UUID):Change{
    return changeRepository.getChange(id)
}
     fun updateChange(change:Change) = changeRepository.updateChange(change)
}