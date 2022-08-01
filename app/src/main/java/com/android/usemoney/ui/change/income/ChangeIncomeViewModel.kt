package com.android.usemoney.ui.change.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.data.model.Category
import com.android.usemoney.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class ChangeIncomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {


}