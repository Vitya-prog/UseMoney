package com.android.usemoney.ui.bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.data.model.Bill
import com.android.usemoney.repository.BillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val billRepository: BillRepository
) : ViewModel() {

    suspend fun getBills():List<Bill>{
        val billList = viewModelScope.async {
            billRepository.getBill()
        }
        return billList.await()
    }
    fun addBill(bill: Bill){
        billRepository.addBill(bill)
    }
}