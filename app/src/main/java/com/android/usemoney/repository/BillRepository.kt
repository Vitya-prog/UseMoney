package com.android.usemoney.repository


import com.android.usemoney.data.database.dao.BillDao
import com.android.usemoney.data.model.Bill
import java.util.concurrent.ExecutorService
import javax.inject.Inject
private const val TAG ="BillRepository"
class BillRepository @Inject constructor(
    private val billDao: BillDao,
    private val executor: ExecutorService,

) {
     suspend fun getBill():List<Bill> = billDao.getBills()

     fun addBill(bill:Bill) {
       executor.execute {
           billDao.addBill(bill)
       }
    }


}