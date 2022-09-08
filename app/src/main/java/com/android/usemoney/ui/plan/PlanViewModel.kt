package com.android.usemoney.ui.plan


import androidx.lifecycle.*
import com.android.usemoney.data.local.Plan
import com.android.usemoney.repository.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {
    val plans = planRepository.getPlan()

    fun deletePlan(plan:Plan){
        planRepository.deletePlan(plan)
    }

}