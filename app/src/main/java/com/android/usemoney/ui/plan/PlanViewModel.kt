package com.android.usemoney.ui.plan


import androidx.lifecycle.*
import com.android.usemoney.data.model.Plan
import com.android.usemoney.repository.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {

    suspend fun getPlanList():List<Plan> {
         val planListDeferred = viewModelScope.async(Dispatchers.IO) {
             planRepository.getPlan()
         }
        return  planListDeferred.await()
    }


    fun addPlan(plan: Plan){
        planRepository.addPlan(plan)
    }
    fun updatePlan(plan: Plan){
        planRepository.updatePlan(plan)
    }
      fun setStartValue(name:String,plan: Plan){
            viewModelScope.launch(Dispatchers.IO) {
                plan.startValue = planRepository.getStartValue(name).distinct().sum()
            }
          planRepository.updatePlan(plan)
    }
}