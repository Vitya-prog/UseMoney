package com.android.usemoney.ui.change.income

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.data.model.Category
import com.android.usemoney.databinding.FragmentChangeIncomeBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.cost.ChangeCostViewModel
import com.android.usemoney.ui.change.cost.ChangeFragmentCost
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "ChangeFragmentIncome"
@AndroidEntryPoint
class ChangeFragmentIncome : Fragment() {
     private lateinit var binding: FragmentChangeIncomeBinding
     private var i = 0
    companion object {
        fun newInstance() = ChangeFragmentIncome()
    }

    private val incomeViewModel: ChangeCostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeIncomeBinding.inflate(inflater,container,false)
        initPieChart()
        i=0
        loadData()
        return binding.root
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            var sum = incomeViewModel.getIncomeSum()
            if(sum == null){
                sum = 0.0
            }
            setDataToPieChart(sum,incomeViewModel.getIncomeCategories())
                incomeViewModel.getIncomeCategory().collect { item ->
                    item.value = incomeViewModel.getChangesList(item.name).sum()
                    incomeViewModel.updateCategory(item)
                    showCategory(item)
                }

        }

    }

    private fun showCategory(category: Category) {

                val categoryButton = Button(requireContext(), null, R.style.categoryButton)
                categoryButton.text = "Test"
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = 50f
                shape.setColor(Color.parseColor(category.color))
                val drawable = resources.getDrawable(category.icon)
                val layerDrawable = LayerDrawable(arrayOf(shape, drawable))

                categoryButton.layoutParams = params
                categoryButton.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    layerDrawable,
                    null,
                    null
                )
                categoryButton.text =
                    "${category.name}\n${category.value.toInt()}\u20B4"
                categoryButton.textSize = 12f
                categoryButton.textAlignment = View.TEXT_ALIGNMENT_CENTER
                categoryButton.setOnClickListener {
                    val intent = Intent(context, AddActivity::class.java)
                    intent.putExtra("edit","category")
                    intent.putExtra("editCategory","${category.id}")
                    context?.startActivity(intent)
                }
                categoryButton.setOnLongClickListener {
                   incomeViewModel.deleteCategory(category)
                    MainActivity.closeActivity(activity as MainActivity)
                    true
                }
                params.setMargins(20, 0, 0, 0)
                if (i <= 2) {
                    params.setMargins(175, 0, 0, 0)
                    binding.incomeCategoriesTop.addView(categoryButton)
                }
                if ((i >= 3) && (i <= 5)) {
                    params.setMargins(175, 0, 0, 0)
                    binding.incomeCategoriesBottom.addView(categoryButton)
                }
                if (i >= 6) {
                    params.setMargins(20, 0, 0, 0)
                    binding.incomeCategoriesContainer.addView(categoryButton)
                }
        i++
            }


    override fun onStart() {
        super.onStart()
        binding.pieChartIncome.setOnClickListener {
            val fragment = ChangeFragmentCost()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.changeContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }

    }
    private fun initPieChart() {
        binding.pieChartIncome.apply {
            setUsePercentValues(true)
            description.text = ""
            isDrawHoleEnabled = false
            setTouchEnabled(false)
            setDrawEntryLabels(false)
            setExtraOffsets(20f, 0f, 20f, 20f)
            isRotationEnabled = false
            legend.isEnabled = false
        }

    }
    private fun setDataToPieChart(sum:Double,name:List<Category>) {
        val dataEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int> = ArrayList()
        binding.pieChartIncome.setUsePercentValues(true)
        //set data
        if (sum == 0.0){
            dataEntries.add(PieEntry(100f))
            colors.add(Color.parseColor("#8E8E8E"))
        } else {
            for (i in name.indices){
                if (name[i].value != 0.0){
                    dataEntries.add(PieEntry(((name[i].value / sum) * 100).toFloat()))
                    colors.add(Color.parseColor(name[i].color))
                }
            }
        }
        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        binding.pieChartIncome.data = data
        data.setValueTextSize(15f)
        binding.pieChartIncome.apply {
            setExtraOffsets(5f, 10f, 5f, 5f)
            animateY(1400, Easing.EaseInOutQuad)
        }
        //create hole in center
        binding.pieChartIncome.apply {
            holeRadius = 80f
            transparentCircleRadius = 55f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
        }
        //add text in center
        binding.pieChartIncome.apply {
            setDrawCenterText(true)
            setCenterTextSize(20f)
            setCenterTextColor(Color.parseColor("#983300"))
            centerText = "+${sum.toInt()}\u20B4"
        }




        binding.pieChartIncome.invalidate()

    }


}