package com.android.usemoney.ui.add.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.usemoney.data.local.Category
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
):ViewModel() {
    private val categoryId = MutableLiveData<UUID>()

    var category: LiveData<Category> =
Transformations.switchMap(categoryId){ id->
    categoryRepository.getCategory(id)
}
    fun loadCategory(id: UUID){
        categoryId.value = id
    }

    fun addCategory(category: Category){
        categoryRepository.addCategory(category)
    }

    fun updateCategory(category: Category){
        categoryRepository.updateCategory(category)
    }
}