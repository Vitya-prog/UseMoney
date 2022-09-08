package com.android.usemoney.ui.bill

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.android.usemoney.data.local.Bill
import com.android.usemoney.repository.BillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val billRepository: BillRepository
) : ViewModel() {

    val bills = billRepository.getBill()

    fun deleteBill(bill: Bill,context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag("PrivatBank")
        billRepository.deleteBill(bill)
    }
}