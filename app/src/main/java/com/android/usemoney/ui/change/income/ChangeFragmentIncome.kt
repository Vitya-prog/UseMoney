package com.android.usemoney.ui.change.income

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.R
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.ui.change.AccountDialogFragment
import com.android.usemoney.ui.change.DateDialogFragment
import com.android.usemoney.ui.change.cost.ChangeFragmentCost
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

private const val TAG = "ChangeFragmentIncome"
class ChangeFragmentIncome : Fragment() {
    private lateinit var pieChart: PieChart
    private lateinit var accountTextView:TextView
    private lateinit var dateTextView: TextView
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button

    companion object {
        fun newInstance() = ChangeFragmentIncome()
    }

    private val incomeViewModel: ChangeIncomeViewModel by lazy {
        ChangeIncomeViewModel().also {
            ViewModelProvider(this)[ ChangeIncomeViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_change_income, container, false)
        pieChart = view.findViewById(R.id.pieChartIncome)
        accountTextView = view.findViewById(R.id.accountTextViewIncome)
        dateTextView = view.findViewById(R.id.dateTextViewIncome)
        button7 = view.findViewById(R.id.button7)
        button8 = view.findViewById(R.id.button8)
        button9 = view.findViewById(R.id.button9)
        initPieChart()
        loadData()
        return view
    }

private fun loadData(){
    viewLifecycleOwner.lifecycleScope.launch{
        val categoryList = incomeViewModel.getIncomeCategories()
        loadValue(categoryList)
    }
}
    private suspend fun loadValue(name: List<CategoryEntity>){
        var sum = 0.0
        for (i in 0..2) {
            name[i].value = incomeViewModel.getChangesList(name[i].name).distinct().sum()
            sum += incomeViewModel.getChangesList(name[i].name).distinct().sum()
        }
        setDataToPieChart(sum,name)
        button7.text = "${name[0].name}\n${name[0].value.toInt()}\u20B4"
        button8.text = "${name[1].name}\n${name[1].value.toInt()}\u20B4"
        button9.text = "${name[2].name}\n${name[2].value.toInt()}\u20B4"
    }
    override fun onStart() {
        super.onStart()
        pieChart.setOnClickListener {
            val fragment = ChangeFragmentCost.newInstance()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()

        }
        accountTextView.setOnClickListener {
            AccountDialogFragment().show(parentFragmentManager,"AccountDialogFragment")
        }
        dateTextView.setOnClickListener {
            DateDialogFragment().show(parentFragmentManager,"DateDialogFragment")
        }
    }
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.legend.isEnabled = false

    }
    private fun setDataToPieChart(sum:Double,name: List<CategoryEntity>) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int> = ArrayList()

        if (sum == 0.0){
            dataEntries.add(PieEntry(100f))
            colors.add(Color.parseColor("#8E8E8E"))
        } else {
            val arrayColor = arrayOf("#DAA520","#87CEFA","#20B2AA")
            for (i in name.indices){
                if (name[i].value != 0.0){
                    dataEntries.add(PieEntry(((name[i].value / sum) * 100).toFloat()))
                    colors.add(Color.parseColor(arrayColor[i]))
                }
            }
        }

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 80f
        pieChart.transparentCircleRadius = 55f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true)
        pieChart.setCenterTextSize(20f)
        pieChart.setCenterTextColor(Color.parseColor("#008000"))
        pieChart.centerText = "+${sum.toInt()}\u20B4"



        pieChart.invalidate()

    }

}