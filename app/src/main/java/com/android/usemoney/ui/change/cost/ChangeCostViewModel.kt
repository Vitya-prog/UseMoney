package com.android.usemoney.ui.change.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.repository.UseMoneyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class ChangeCostViewModel:ViewModel() {
    private val useMoneyRepository = UseMoneyRepository.get()

    suspend fun getChangesList(name:String):List<Double>{
        val changeList = viewModelScope.async {
            useMoneyRepository.getChangeList(name)
        }
        return changeList.await()
    }
    suspend fun getCostCategories():List<CategoryEntity>{
        val categoryList = viewModelScope.async(Dispatchers.IO) {
            useMoneyRepository.getCostCategories()
        }
        return categoryList.await()
    }
    fun addCategory(category: CategoryEntity){
        useMoneyRepository.addCategory(category)
    }
}