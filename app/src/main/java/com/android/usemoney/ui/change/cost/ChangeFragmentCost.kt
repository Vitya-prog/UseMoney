package com.android.usemoney.ui.change.cost





import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.usemoney.R
import com.android.usemoney.data.local.Category
import com.android.usemoney.databinding.FragmentChangeCostBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.income.ChangeFragmentIncome
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private const val TAG ="ChangeFragmentCost"
@AndroidEntryPoint
class ChangeFragmentCost : Fragment() {
    private lateinit var binding:FragmentChangeCostBinding
    private val costViewModel: ChangeCostViewModel by viewModels()
    private var i = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeCostBinding.inflate(inflater,container,false)
        initPieChart()
        loadData()
        return binding.root
    }




    private fun showCategory(category:Category,size:Int){
        val button = Button(requireContext(), null, R.style.categoryButton)
        val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = 50f
                shape.setColor(Color.parseColor(category.color))
                val drawable = resources.getDrawable(resources.getIdentifier(category.icon,"drawable",activity?.packageName))
                val layerDrawable = LayerDrawable(arrayOf(shape, drawable))
                params.setMargins(20, 0, 0, 0)
                button.layoutParams = params
                button.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    layerDrawable,
                    null,
                    null
                )
                button.text =
                    "${category.name}\n${(category.value*100).roundToInt() / 100.0}"
                button.textSize = 12f
                button.textAlignment = View.TEXT_ALIGNMENT_CENTER
                button.setOnClickListener {
                    val intent = Intent(context, AddActivity::class.java)
                    intent.putExtra("edit","category")
                    intent.putExtra("editCategory","${category.id}")
                    context?.startActivity(intent)
                }
                button.setOnLongClickListener {
                    costViewModel.deleteCategory(category)
                    true
                }
                if ((i <= 2) && (i < size)) {
                    params.setMargins(175, 0, 0, 0)
                    binding.costCategoriesTop.addView(button)
                }
                if ((i >= 3) && (i <= 5) && (i < size)) {
                    params.setMargins(175, 0, 0, 0)
                    binding.costCategoriesBottom.addView(button)
                }
                if ((i >= 6) && (i < size)) {
                    binding.costCategoriesContainer.addView(button)
                }
        i++

    }
    private fun loadData() {
        i=0
        val dateFrom = SimpleDateFormat("dd.MM.yyyy").parse("${binding.dateFromTextView.text}")!!
        val dateTo = SimpleDateFormat("dd.MM.yyyy").parse("${binding.dateToTextView.text}")!!
        costViewModel.selectDate(dateFrom.time)
        costViewModel.dateFrom.observe(
            viewLifecycleOwner
        ){
            costViewModel.getCostCategories(dateTo.time,it).observe(
                viewLifecycleOwner
            )  { categories->
                setDataToPieChart(categories)
                categories.forEach { category ->
                    showCategory(category,categories.size)
                }
            }
        }

}




    companion object {
        fun newInstance(): ChangeFragmentCost {
          return ChangeFragmentCost()
        }
    }

    override fun onStart() {
       super.onStart()
        binding.pieChartCost.setOnClickListener {
                val fragment = ChangeFragmentIncome()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.changeContainerView,fragment)
                    .commit()
        }
        binding.dateToTextView.setOnClickListener {
        setDate(binding.dateToTextView)
        }
        binding.dateFromTextView.setOnClickListener {
            setDate(binding.dateFromTextView)
        }

    }
    private fun setDate(view: TextView) {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        val dialogFragment = DatePickerDialog(requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val d = if (day < 10) "0$day" else "$day"
                val m = if (month <= 9) "0${month + 1}" else "${month + 1}"
                view.text = "$d.$m.$year"
                costViewModel.selectDate(SimpleDateFormat("dd.MM.yyyy").parse("${binding.dateFromTextView.text}")!!.time)
            },initialYear,initialMonth,initialDay)
        dialogFragment.show()
    }
    private fun initPieChart() {
        binding.pieChartCost.apply {
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
    private fun setDataToPieChart(name:List<Category>) {
        val sum = name.sumOf { it.value }
        val dataEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int> = ArrayList()
        binding.pieChartCost.setUsePercentValues(true)
        //set data
        if (sum == 0.0){
            dataEntries.add(PieEntry(100f))
            colors.add(Color.parseColor("#8E8E8E"))
        } else {
            for(i in name.indices) {
                if (name[i].value != 0.0) {
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
        binding.pieChartCost.data = data
        data.setValueTextSize(15f)
        binding.pieChartCost.apply {
            setExtraOffsets(5f, 10f, 5f, 5f)
            animateY(1400, Easing.EaseInOutQuad)
        }
        //create hole in center
        binding.pieChartCost.apply {
            holeRadius = 80f
            transparentCircleRadius = 55f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
        }
        //add text in center
        binding.pieChartCost.apply {
            setDrawCenterText(true)
            setCenterTextSize(20f)
            setCenterTextColor(Color.parseColor("#FF0000"))
            centerText = "-${(sum*100.0).roundToInt()/100.0}"
        }

        binding.pieChartCost.invalidate()

    }

}