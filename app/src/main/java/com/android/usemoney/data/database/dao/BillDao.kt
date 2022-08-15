package com.android.usemoney.data.database.dao

import androidx.room.*
import com.android.usemoney.data.model.Bill

@Dao
interface BillDao {
 @Query("SELECT * FROM bill")
 suspend fun getBills():List<Bill>

 @Insert
 fun addBill(bill:Bill)

 @Update
 fun updateBill(bill: Bill)

 @Delete
 fun deleteBill(bill:Bill)
}