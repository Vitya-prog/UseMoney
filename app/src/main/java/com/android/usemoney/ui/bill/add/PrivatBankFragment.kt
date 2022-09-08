package com.android.usemoney.ui.bill.add

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.usemoney.data.local.Bill
import com.android.usemoney.databinding.FragmentPrivatBankBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
            val bill = Bill(UUID.randomUUID(), "${binding.cardEditText.text}", 0.0)
                privatBankViewModel.addBill(
                    bill,
                    binding.idEditText.text.toString(),
                    binding.cardEditText.text.toString(),
                    date,
                    binding.passwordEditText.text.toString(),
                    requireContext()
                )
                activity?.finish()
        }
        binding.dateEditText.setOnClickListener {
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { view: DatePicker, year: Int, month: Int, day: Int ->
                    val d = if (day < 10) "0$day" else "$day"
                    val m = if (month < 10) "0${month + 1}" else "${month + 1}"
                    binding.dateEditText.setText("$d.$m.$year")
                   date = "$d.$m.$year"
                },initialYear,initialMonth,initialDay)

            dialogFragment.show()
        }
        binding.createIdButton.setOnClickListener {
            val intent = Intent(context, AddMerchantActivity::class.java)
            context?.startActivity(intent)
        }
    }

}


