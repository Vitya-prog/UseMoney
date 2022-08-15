package com.android.usemoney.ui.change

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.R
import com.android.usemoney.databinding.FragmentChartBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.cost.ChangeCostViewModel
import com.android.usemoney.ui.change.cost.ChangeFragmentCost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ChartFragment : Fragment() {
  private lateinit var binding:FragmentChartBinding
    private val incomeViewModel: ChartFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater,container,false)
        loadData()
        return binding.root
    }
private fun loadData(){
    viewLifecycleOwner.lifecycleScope.launch {
        val sum = incomeViewModel.getSum()
        if (sum < 0.0){
            binding.accountTextViewCost.setTextColor(Color.parseColor("#FF0000"))
            binding.accountTextViewCost.text ="Счёт: ${sum.toInt()}\u20B4"
        }
        if(sum > 0.0){
            binding.accountTextViewCost.setTextColor(Color.parseColor("#8E8E8E"))
            binding.accountTextViewCost.text ="Счёт: +${sum.toInt()}₴"
        }
        if (sum == 0.0){
            binding.accountTextViewCost.text ="Счёт: ${sum.toInt()}₴"
        }
    }
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