package com.android.usemoney.ui.add.plan

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Plan
import com.android.usemoney.repository.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
@HiltViewModel
class AddPlanViewModel @Inject constructor(
    private val planRepository: PlanRepository
): ViewModel() {
   suspend fun getIconCategories():List<Category>{
        val iconList = CoroutineScope(Dispatchers.IO).async {
           planRepository.getIconCategories()
        }
        return iconList.await()
    }
    fun addPlan(plan: Plan){
        planRepository.addPlan(plan)
    }
}