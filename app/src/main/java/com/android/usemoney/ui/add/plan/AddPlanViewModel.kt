package com.android.usemoney.ui.add.plan

import androidx.lifecycle.ViewModel
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Plan
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
   val iconCategories = planRepository.getIconCategories()
    fun addPlan(plan: Plan){
        planRepository.addPlan(plan)
    }
}