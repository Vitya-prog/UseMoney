package com.android.usemoney.ui.bill

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.usemoney.adapters.BillAdapter
import com.android.usemoney.data.local.Bill
import com.android.usemoney.databinding.FragmentCardBinding
import com.android.usemoney.ui.bill.add.AddBillActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BillFragment : Fragment(),BillAdapter.OnBillLongListener {
    @Inject
    lateinit var shape:GradientDrawable
    private val billViewModel: BillViewModel by viewModels()
    private lateinit var binding:FragmentCardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater,container,false)
        binding.billRecyclerView.layoutManager = LinearLayoutManager(context)
       loadData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.addBillButton.setOnClickListener {
            val intent = Intent(context, AddBillActivity::class.java)
            intent.putExtra("add","change")
            context?.startActivity(intent)
        }
    }
    private fun loadData() {
        billViewModel.bills.observe(
            viewLifecycleOwner
        ) {
           updateUI(it)
        }

    }

    private fun updateUI(bills: List<Bill>) {
        val adapter = BillAdapter(requireContext(),this)
        adapter.submitList(bills)
        binding.billRecyclerView.adapter = adapter
    }

    override fun deleteBill(bill: Bill) {
        billViewModel.deleteBill(bill, requireContext())
    }
}