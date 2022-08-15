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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.data.model.Category
import com.android.usemoney.databinding.FragmentChangeCostBinding
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.ui.change.income.ChangeFragmentIncome
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

private const val TAG ="ChangeFragmentCost"
@AndroidEntryPoint
class ChangeFragmentCost : Fragment() {
    private lateinit var binding:FragmentChangeCostBinding
    private val costViewModel: ChangeCostViewModel by viewModels()
    private var costCategoryList: List<Category> = emptyList()
    private var i = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeCostBinding.inflate(inflater,container,false)
        initPieChart()
        i=0
        loadData()
        return binding.root
    }



    private fun showCategory(category:Category){
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
                params.setMargins(20, 0, 0, 0)
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
                    costViewModel.deleteCategory(category)
                    MainActivity.closeActivity(activity as MainActivity)
                    true
                }
                if (i <= 2) {
                    params.setMargins(175, 0, 0, 0)
                    binding.costCategoriesTop.addView(categoryButton)
                }
                if ((i >= 3) && (i <= 5)) {
                    params.setMargins(175, 0, 0, 0)
                    binding.costCategoriesBottom.addView(categoryButton)
                }
                if (i >= 6) {
                    binding.costCategoriesContainer.addView(categoryButton)
                }
        i++

    }

    private fun loadData() {

        viewLifecycleOwner.lifecycleScope.launch {
            costCategoryList = costViewModel.getCostCategories()
            createCategory(costCategoryList)
            var sum = costViewModel.getCostSum()
            if(sum == null){
                sum = 0.0
            }
            setDataToPieChart(sum, costViewModel.getCostCategories())

                costViewModel.getCostCategory().collect { item ->
                    item.value = costViewModel.getChangesList(item.name).sum()
                    costViewModel.updateCategory(item)
                    showCategory(item)
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
    private fun setDataToPieChart(sum:Double,name:List<Category>) {
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
            centerText = "-${sum.toInt()}\u20B4"
        }




        binding.pieChartCost.invalidate()

    }
    private fun createCategory(categories:List<Category>) {
        if (categories.isEmpty()){
            costViewModel.addCategory(Category(UUID.randomUUID(),"Подарки","Расходы",0.0, R.drawable.category_gift_icon,"#C71585"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Еда","Расходы",0.0,R.drawable.category_food_icon,"#228B22"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Семья","Расходы",0.0,R.drawable.category_family_icon,"#FF8A65"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Кафе","Расходы",0.0,R.drawable.category_cafe_icon,"#FFF176"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Здоровье","Расходы",0.0,R.drawable.category_health_icon,"#4DD0E1"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Транспорт","Расходы",0.0,R.drawable.category_transport_icon,"#FFDEAD"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Зарплата","Доходы",0.0,R.drawable.category_salary_icon,"#DAA520"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Депозит","Доходы",0.0,R.drawable.category_deposit_icon,"#87CEFA"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Долг","Доходы",0.0,R.drawable.category_debt_icon,"#20B2AA"))
            costViewModel.addCategory(Category(UUID.randomUUID(),"Неизвестен","",0.0, R.drawable.unknown_icon,"#A9A9A9"))

        }
    }

}