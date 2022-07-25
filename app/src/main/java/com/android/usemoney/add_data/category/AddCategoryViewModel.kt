package com.android.usemoney.add_data.category

import androidx.lifecycle.ViewModel
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.repository.UseMoneyRepository

class AddCategoryViewModel:ViewModel() {
    private val useMoneyRepository = UseMoneyRepository.get()
    fun addCategory(category: CategoryEntity){
        useMoneyRepository.addCategory(category)
    }
}