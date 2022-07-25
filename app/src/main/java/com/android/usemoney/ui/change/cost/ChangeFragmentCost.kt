package com.android.usemoney.ui.change.cost


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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.R
import com.android.usemoney.add_data.AddActivity
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.ui.change.AccountDialogFragment
import com.android.usemoney.ui.change.DateDialogFragment
import com.android.usemoney.ui.change.income.ChangeFragmentIncome
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import java.util.*

private const val TAG ="ChangeFragmentCost"
class ChangeFragmentCost : Fragment() {
    private lateinit var accountTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var addCostCategoryButton: Button
    private lateinit var pieChart: PieChart
    private lateinit var categoriesContainer: LinearLayout
    private lateinit var button1:Button
    private lateinit var button2:Button
    private lateinit var button3:Button
    private lateinit var button4:Button
    private lateinit var button5:Button
    private lateinit var button6:Button
    private val costViewModel: ChangeCostViewModel by lazy {
        ChangeCostViewModel().also {
            ViewModelProvider(this)[ ChangeCostViewModel::class.java]
        }
    }


    private var categoryList: List<CategoryEntity> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_cost, container, false)
        pieChart = view.findViewById(R.id.pieChartCost)
        accountTextView = view.findViewById(R.id.accountTextViewCost)
        dateTextView = view.findViewById(R.id.dateTextViewCost)
        addCostCategoryButton = view.findViewById(R.id.add_cost_category_button)
        categoriesContainer = view.findViewById(R.id.categoriesContainer)
        button1 = view.findViewById(R.id.button1)
        button2 = view.findViewById(R.id.button2)
        button3 = view.findViewById(R.id.button3)
        button4 = view.findViewById(R.id.button4)
        button5 = view.findViewById(R.id.button5)
        button6 = view.findViewById(R.id.button6)
        initPieChart()
        loadData()
        setListeners()
        return view
    }


    private fun showCategory(){
        if (categoryList.size > 6) {
            for(i in 6..categoryList.size - 1) {
                val categoryButton = Button(requireContext(), null, R.style.categoryButton)
                categoryButton.text = "Test"
                val params  = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = 50f
                shape.setColor(Color.parseColor(categoryList[i].color))
                val drawable = resources.getDrawable(categoryList[i].icon)
                val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
                params.setMargins(20,0,0,0)
                categoryButton.layoutParams = params
                categoryButton.setCompoundDrawablesWithIntrinsicBounds(null,layerDrawable,null,null)
                categoryButton.text = "${categoryList[i].name}\n${categoryList[i].value.toInt()}\u20B4"
                categoryButton.textSize = 12f
                categoryButton.textAlignment = View.TEXT_ALIGNMENT_CENTER
                categoryButton.setOnClickListener {
                    Log.d(TAG, categoryList[i].name)
                }
                categoriesContainer.addView(categoryButton)
            }
        }
    }

        private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            categoryList = costViewModel.getCostCategories()
               createCategory(categoryList)
               loadValue(categoryList)
        }

}

    private suspend fun loadValue(name:List<CategoryEntity>) {
        var sum = 0.0
        for (i in categoryList.indices) {
            name[i].value = costViewModel.getChangesList(name[i].name).distinct().sum()
            sum += costViewModel.getChangesList(name[i].name).distinct().sum()
        }
        showCategory()
        button1.text = "${name[0].name}\n${name[0].value.toInt()}\u20B4"
        button2.text = "${name[1].name}\n${name[1].value.toInt()}\u20B4"
        button3.text = "${name[2].name}\n${name[2].value.toInt()}\u20B4"
        button4.text = "${name[3].name}\n${name[3].value.toInt()}\u20B4"
        button5.text = "${name[4].name}\n${name[4].value.toInt()}\u20B4"
        button6.text = "${name[5].name}\n${name[5].value.toInt()}\u20B4"

        setDataToPieChart(sum,name)

    }


    companion object {
        fun newInstance(): ChangeFragmentCost {
          return ChangeFragmentCost()
        }

    }

    override fun onStart() {
        super.onStart()
        pieChart.setOnClickListener {
           val fragment = ChangeFragmentIncome.newInstance()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()

        }
        accountTextView.setOnClickListener{
            AccountDialogFragment().show(parentFragmentManager,"AccountDialogFragment")
        }
        dateTextView.setOnClickListener{
            DateDialogFragment().show(parentFragmentManager,"DateDialogFragment")
        }
        addCostCategoryButton.setOnClickListener{
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","category")
            context?.startActivity(intent)
        }

    }
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        pieChart.setExtraOffsets(20f, 0f, 20f, 20f)
        pieChart.isRotationEnabled = false
        pieChart.legend.isEnabled = false

    }
    private fun setDataToPieChart(sum:Double,name:List<CategoryEntity>) {
        val dataEntries = ArrayList<PieEntry>()
        val colors: ArrayList<Int> = ArrayList()
        pieChart.setUsePercentValues(true)
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
        pieChart.setCenterTextColor(Color.parseColor("#FF0000"))
        pieChart.centerText = "-${sum.toInt()}\u20B4"



        pieChart.invalidate()

    }
    private fun createCategory(categories:List<CategoryEntity>) {
        if (categories.isEmpty()){
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Подарки","Расходы",0.0, R.drawable.category_gift_icon,"#C71585"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Еда","Расходы",0.0,R.drawable.category_food_icon,"#228B22"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Семья","Расходы",0.0,R.drawable.category_family_icon,"#FF8A65"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Кафе","Расходы",0.0,R.drawable.category_cafe_icon,"#FFF176"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Здоровье","Расходы",0.0,R.drawable.category_health_icon,"#4DD0E1"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Транспорт","Расходы",0.0,R.drawable.category_transport_icon,"#FFDEAD"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Зарплата","Доходы",0.0,R.drawable.category_salary_icon,"#DAA520"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Депозит","Доходы",0.0,R.drawable.category_deposit_icon,"#87CEFA"))
            costViewModel.addCategory(CategoryEntity(UUID.randomUUID(),"Долг","Доходы",0.0,R.drawable.category_debt_icon,"#20B2AA"))

        }
    }
    private fun setListeners(){
        button1.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
        button2.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
        button3.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
        button4.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
        button5.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
        button6.apply{
            setOnClickListener {
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","change")
                context?.startActivity(intent)
            }
            setOnLongClickListener{
                val intent = Intent(context, AddActivity::class.java)
                intent.putExtra("add","category")
                context?.startActivity(intent)
                true
            }
        }
    }
}