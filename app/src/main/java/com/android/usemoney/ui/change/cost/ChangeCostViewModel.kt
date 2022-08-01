package com.android.usemoney.ui.change.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.data.model.Category
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class ChangeCostViewModel @Inject constructor(
  private val categoryRepository: CategoryRepository
) :ViewModel() {

    suspend fun getChangesList(name:String):List<Double>{
        val changeList = viewModelScope.async {
            categoryRepository.getChangeList(name)
        }
        return changeList.await()
    }
    suspend fun getCostCategories():List<Category>{
        val categoryList = viewModelScope.async(Dispatchers.IO) {
            categoryRepository.getCostCategories()
        }
        return categoryList.await()
    }
    fun addCategory(category: Category){
        categoryRepository.addCategory(category)
    }
    suspend fun getIncomeCategories():List<Category>{
        val changeList = viewModelScope.async {
            categoryRepository.getIncomeCategories()
        }
        return changeList.await()
    }
    fun updateCategory(category: Category){
        categoryRepository.updateCategory(category)
    }
}