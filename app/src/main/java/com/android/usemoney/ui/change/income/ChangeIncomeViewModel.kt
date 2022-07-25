package com.android.usemoney.ui.change.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.repository.UseMoneyRepository
import kotlinx.coroutines.async

class ChangeIncomeViewModel : ViewModel() {
    private val useMoneyRepository = UseMoneyRepository.get()

    suspend fun getIncomeCategories():List<CategoryEntity>{
        val changeList = viewModelScope.async {
            useMoneyRepository.getIncomeCategories()

        }
        return changeList.await()
    }
    suspend fun getChangesList(name:String):List<Double>{
        val changeList = viewModelScope.async {
            useMoneyRepository.getChangeList(name)
        }
        return changeList.await()
    }
}