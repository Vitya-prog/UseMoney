package com.android.usemoney.ui.bill.add


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.usemoney.data.local.Bill
import com.android.usemoney.repository.BillRepository
import com.android.usemoney.workers.TransactionsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
private const val TAG = "PrivatBankViewModel"

@HiltViewModel
class PrivatBankViewModel @Inject constructor(
  private val billRepository: BillRepository,
): ViewModel() {

fun addBill(bill:Bill,
            id:String,
            card:String,
            date:String,
            password:String,
            context: Context) {
        billRepository.addBill(bill)
        val data = Data.Builder()
            .putString("keyId", id)
            .putString("keyCard", card)
            .putString("keyDate", date)
            .putString("keyPassword", password)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(
            PeriodicWorkRequestBuilder<TransactionsWorker>(1, TimeUnit.SECONDS)
                .setInputData(data)
                .addTag("PrivatBank")
                .build()
        )
    }


}