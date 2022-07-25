package com.android.usemoney.ui.plan


import androidx.lifecycle.*
import com.android.usemoney.entities.PlanEntity
import com.android.usemoney.repository.UseMoneyRepository
import kotlinx.coroutines.*

class PlanViewModel : ViewModel() {

    private val useMoneyRepository = UseMoneyRepository.get()
    suspend fun getPlanList():List<PlanEntity> {
         val planListDeferred = viewModelScope.async(Dispatchers.IO) {
             useMoneyRepository.getPlan()
         }
        return  planListDeferred.await()
    }


    fun addPlan(plan:PlanEntity){
        useMoneyRepository.addPlan(plan)
    }
    fun updatePlan(plan: PlanEntity){
        useMoneyRepository.updatePlan(plan)
    }
      fun setStartValue(name:String,plan: PlanEntity){
            viewModelScope.launch(Dispatchers.IO) {
                plan.startValue = useMoneyRepository.getStartValue(name).distinct().sum()
            }
          useMoneyRepository.updatePlan(plan)
    }
}