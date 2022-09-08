package com.android.usemoney.ui.change


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.databinding.FragmentChartBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.cost.ChangeCostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "ChartFragment"
@AndroidEntryPoint
class ChartFragment : Fragment() {
  private lateinit var binding:FragmentChartBinding
    private val chartViewModel: ChangeCostViewModel by viewModels()
    private val currency = listOf("$","₴","€","£")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater,container,false)
        loadData()
        chartViewModel.currency.observe(
            viewLifecycleOwner
        ){
            Log.d(TAG,it.toString())
        }
        return binding.root
    }
private fun loadData(){
    viewLifecycleOwner.lifecycleScope.launch {
        val sum = chartViewModel.getSum()
        if (sum < 0.0){
            binding.accountTextViewCost.setTextColor(Color.parseColor("#FF0000"))
            binding.accountTextViewCost.text ="Всего: ${sum.toInt()}"
        }
        if(sum > 0.0){
            binding.accountTextViewCost.setTextColor(Color.parseColor("#8E8E8E"))
            binding.accountTextViewCost.text ="Всего: +${sum.toInt()}"
        }
        if (sum == 0.0){
            binding.accountTextViewCost.text ="Всего: ${sum.toInt()}"
        }
    }
}
    override fun onStart() {
        super.onStart()
        binding.addCostCategoryButton.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add", "category")
            context?.startActivity(intent)
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

               binding.currencyTextView.text = currency[p2]
                when (p2) {
                    0->chartViewModel.updateCurrency(0.027)
                    1->chartViewModel.updateCurrency(1.0)
                    2->chartViewModel.updateCurrency(0.027)
                    3->chartViewModel.updateCurrency(0.024)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
               Log.d(TAG,"Nothing Select")
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               Log.d(TAG,"Click!")
            }

        }
    }


}