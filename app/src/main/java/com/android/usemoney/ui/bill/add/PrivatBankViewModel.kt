package com.android.usemoney.ui.bill.add


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
import com.android.usemoney.repository.BillRepository
import com.android.usemoney.repository.ChangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import okhttp3.*
import java.util.*
import javax.inject.Inject
private const val TAG = "PrivatBankViewModel"

@HiltViewModel
class PrivatBankViewModel @Inject constructor(
  private val changeRepository: ChangeRepository
): ViewModel() {

    suspend fun searchChange(value:Double, date: Date, description:String):List<Category?>{
        val res = viewModelScope.async {
            changeRepository.getChangeByParam(value,date,description)
        }
        return res.await()
    }

    fun addChange(change: Change){
        changeRepository.addChanges(change)
    }
}