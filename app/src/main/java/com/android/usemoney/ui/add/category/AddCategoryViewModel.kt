package com.android.usemoney.ui.add.category

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.model.Category
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
):ViewModel() {

    fun addCategory(category: Category){
        categoryRepository.addCategory(category)
    }
}