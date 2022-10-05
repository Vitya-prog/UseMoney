package com.android.usemoney.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.usemoney.data.local.Bill

@Dao
interface BillDao {
 @Query("SELECT * FROM bill")
 fun getBills():LiveData<List<Bill>>

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun addBill(bill:Bill)

 @Update
 fun updateBill(bill: Bill)

 @Delete
 fun deleteBill(bill:Bill)
}