package com.android.usemoney.ui.change

import androidx.lifecycle.ViewModel
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartFragmentViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
):ViewModel() {
    suspend fun getSum():Double{
        var incomeSum: Double? = categoryRepository.getIncomeSum()
        var costSum: Double? = categoryRepository.getCostSum()
        if (incomeSum == null){
            incomeSum = 0.0
        }
        if(costSum == null){
            costSum = 0.0
        }
      return incomeSum-costSum
    }
}