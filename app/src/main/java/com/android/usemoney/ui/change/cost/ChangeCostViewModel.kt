package com.android.usemoney.ui.change.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.data.model.Category
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    suspend fun getIncomeSum():Double?{
        val sum = viewModelScope.async {
            categoryRepository.getIncomeSum()
        }
        return sum.await()
    }
    suspend fun getCostSum():Double?{
        val sum = viewModelScope.async {
        categoryRepository.getCostSum()
    }
        return sum.await()}

       suspend fun getCostCategories():List<Category>{
        val categoryList = viewModelScope.async {
            categoryRepository.getCostCategories()
        }
        return categoryList.await()

    }

    suspend fun getCostCategory(): Flow<Category> = flow {
        categoryRepository.getCostCategories().forEach {
            emit(it)
        }
    }
    suspend fun getIncomeCategory(): Flow<Category> = flow {
        categoryRepository.getIncomeCategories().forEach {
            emit(it)
        }
    }
    fun addCategory(category: Category){
        categoryRepository.addCategory(category)
    }
    suspend fun getIncomeCategories():List<Category>{
        val categoryList = viewModelScope.async {
            categoryRepository.getIncomeCategories()
        }
       return categoryList.await()

    }
    fun updateCategory(category: Category){
        categoryRepository.updateCategory(category)
    }
    fun deleteCategory(category:Category){
        categoryRepository.deleteCategory(category)
    }
}