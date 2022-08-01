package com.android.usemoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.usemoney.databinding.FragmentChartBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.AccountDialogFragment
import com.android.usemoney.ui.change.DateDialogFragment

class ChartFragment : Fragment() {
  private lateinit var binding:FragmentChartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.accountTextViewCost.setOnClickListener{
            AccountDialogFragment().show(parentFragmentManager,"AccountDialogFragment")
        }
        binding.dateTextViewCost.setOnClickListener{
            DateDialogFragment().show(parentFragmentManager,"DateDialogFragment")
        }
        binding.addCostCategoryButton.setOnClickListener{
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","category")
            context?.startActivity(intent)
        }

    }
}