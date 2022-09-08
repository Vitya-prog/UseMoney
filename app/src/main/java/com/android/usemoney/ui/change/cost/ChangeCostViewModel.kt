package com.android.usemoney.ui.change.cost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.usemoney.data.local.Category
import com.android.usemoney.repository.CategoryRepository
import com.android.usemoney.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeCostViewModel @Inject constructor(
  private val categoryRepository: CategoryRepository,
  private val currencyRepository: CurrencyRepository
) :ViewModel() {

    private val mutableDateFrom = MutableLiveData<Long>()
    val dateFrom:LiveData<Long> get() = mutableDateFrom



    fun selectDate(dateFrom: Long){
        mutableDateFrom.value = dateFrom
    }

    fun updateCurrency(currency: Double){
        categoryRepository.updateCurrency(currency)
    }

    fun getCostCategories(dateTo:Long,dateFrom:Long):LiveData<List<Category>>{
       return categoryRepository.getCostCategories(dateTo,dateFrom)
    }
    val incomeCategories = categoryRepository.getIncomeCategories()
    var currency = currencyRepository.getCurrency()
    fun deleteCategory(category:Category){
        categoryRepository.deleteCategory(category)
    }

    suspend fun getSum():Double{
        val incomeSum: Double = categoryRepository.getIncomeSum() ?: 0.0
        val costSum: Double = categoryRepository.getCostSum() ?: 0.0
        return incomeSum-costSum
    }
}