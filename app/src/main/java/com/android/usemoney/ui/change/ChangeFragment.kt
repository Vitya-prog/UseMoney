package com.android.usemoney.ui.change

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


class ChangeFragment : Fragment() {
    private val listChange = arrayListOf("Одежда","Еда","Лекарства","Отдых")
    private var adapter: ChangeAdapter? = ChangeAdapter()
    private lateinit var pieChart: PieChart
    private lateinit var recycleViewCosts: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change, container, false)
        pieChart = view.findViewById(R.id.pieChart)
        recycleViewCosts = view.findViewById(R.id.recyclerViewCosts)
        recycleViewCosts.layoutManager = LinearLayoutManager(context)
        recycleViewCosts.adapter = adapter
        initPieChart()
        setDataToPieChart()
        return view
    }
private inner class ChangeHolder(view:View):RecyclerView.ViewHolder(view){
    private val imageView:ImageView = itemView.findViewById(R.id.imageView)
    private val name:TextView = itemView.findViewById(R.id.name)
    private val valueChange:TextView = itemView.findViewById(R.id.value)
fun bind(i:Int){
    imageView.setImageResource(R.drawable.icon_category)
    name.text = listChange[i]
    valueChange.text = i.toString()
}

}
private inner class ChangeAdapter:RecyclerView.Adapter<ChangeHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeHolder {
    val view = layoutInflater.inflate(R.layout.list_item_change,parent,false)
    return ChangeHolder(view)
    }

    override fun onBindViewHolder(holder: ChangeHolder, position: Int) {
holder.bind(position)
    }

    override fun getItemCount(): Int =listChange.size

}
    companion object {
        fun newInstance(): ChangeFragment {
          return ChangeFragment()
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
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true

    }
    private fun setDataToPieChart() {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(72f, listChange[0]))
        dataEntries.add(PieEntry(26f, listChange[1]))
        dataEntries.add(PieEntry(2f, listChange[2]))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))
        colors.add(Color.parseColor("#FF8A65"))

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
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Расходы"



        pieChart.invalidate()

    }
}