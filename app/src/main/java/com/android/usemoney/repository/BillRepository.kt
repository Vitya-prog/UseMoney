package com.android.usemoney.repository


import androidx.lifecycle.LiveData
import com.android.usemoney.data.database.dao.BillDao
import com.android.usemoney.data.local.Bill
import java.util.concurrent.ExecutorService
import javax.inject.Inject
private const val TAG ="BillRepository"
class BillRepository @Inject constructor(
    private val billDao: BillDao,
    private val executor: ExecutorService,

) {
     fun getBill(): LiveData<List<Bill>> = billDao.getBills()

     fun addBill(bill:Bill) {
      executor.execute {
        billDao.addBill(bill)
      }
    }

    fun deleteBill(bill: Bill){
        executor.execute {
            billDao.deleteBill(bill)
        }
    }

}