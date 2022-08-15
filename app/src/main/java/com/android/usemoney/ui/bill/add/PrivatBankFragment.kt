package com.android.usemoney.ui.bill.add

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.usemoney.databinding.FragmentPrivatBankBinding
import com.android.usemoney.workers.TransactionsWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "PrivatBankFragment"
@AndroidEntryPoint
class PrivatBankFragment : Fragment() {
    private lateinit var binding: FragmentPrivatBankBinding
    private val privatBankViewModel:PrivatBankViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private var date:String = null.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivatBankBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.addCardButton.setOnClickListener {
            val data = Data.Builder()
                .putString("keyId",binding.idEditText.text.toString())
                .putString("keyCard",binding.cardEditText.text.toString())
                .putString("keyDate",date)
                .putString("keyPassword",binding.passwordEditText.text.toString())
                .build()
            val workManager = WorkManager.getInstance(context!!)
            workManager.enqueue(
                PeriodicWorkRequestBuilder<TransactionsWorker>(1,TimeUnit.SECONDS)
                .setInputData(data)
                .build())
            activity?.finish()
        }
        binding.dateEditText.setOnClickListener {
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { view: DatePicker, year: Int, month: Int, day: Int ->
                    binding.dateEditText.setText("0$day.0$month.$year")
                   date = "0$day.0$month.$year"
                },initialYear,initialMonth,initialDay)

            dialogFragment.show()
        }
        binding.createIdButton.setOnClickListener {
            val intent = Intent(context, AddMerchantActivity::class.java)
            context?.startActivity(intent)
        }
    }

}


