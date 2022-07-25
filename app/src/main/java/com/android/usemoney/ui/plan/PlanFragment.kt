package com.android.usemoney.ui.plan

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.entities.PlanEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.android.usemoney.add_data.AddActivity as AddActivity

private const val TAG ="PlanFragment"
class PlanFragment : Fragment() {
    private val planViewModel: PlanViewModel by lazy {
        PlanViewModel().also {
            ViewModelProvider(this)[PlanViewModel::class.java]
        }
    }
    private var adapter = PlanViewAdapter(emptyList())
    private lateinit var planRecycleView: RecyclerView
    private lateinit var addPlanButton:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)
        addPlanButton = view.findViewById(R.id.addPlanButton)
        planRecycleView = view.findViewById(R.id.planRecycleView)
        planRecycleView.layoutManager = LinearLayoutManager(context)
//        planRecycleView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPlan()
    }

    override fun onStart() {
        super.onStart()
        addPlanButton.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","plan")
            context?.startActivity(intent)
        }
    }
    private fun loadPlan() {
        viewLifecycleOwner.lifecycleScope.launch {
            val plans = planViewModel.getPlanList()
            for (i in plans.indices) {
                planViewModel.setStartValue(plans[i].name, plans[i])
            }
            updateUI(plans)
        }
    }
    private fun updateUI(plans:List<PlanEntity>){
    adapter = PlanViewAdapter(plans)
    planRecycleView.adapter = adapter

}

    private inner class PlanViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val planProgressBar: ProgressBar = itemView.findViewById(R.id.planProgressBar)
        private val startValueTextView: TextView = itemView.findViewById(R.id.startValueTextView)
        private val finishValueTextView: TextView = itemView.findViewById(R.id.finishValueTextView)
        private val planTextView: TextView = itemView.findViewById(R.id.planTextView)
        private val planImageView:ImageView = itemView.findViewById(R.id.planImageView)
        private val datePlanTextView:TextView = itemView.findViewById(R.id.datePlanTextView)

        fun bind(plan:PlanEntity){
           val percent = ((plan.startValue / plan.endValue)*100).toInt()
            planProgressBar.setProgress(percent,false)
          planTextView.text = plan.name
          startValueTextView.text = plan.startValue.toString()
          finishValueTextView.text = plan.endValue.toString()
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(plan.color))
            val drawable = resources.getDrawable(plan.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
          planImageView.setImageDrawable(layerDrawable)
          datePlanTextView.text = "Осталось ${SimpleDateFormat("d").format(plan.dateSecond.time - plan.dateFirst.time)} дней"

        }
    }
    private inner class PlanViewAdapter(var plans :List<PlanEntity>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item_plan,parent,false)
               return PlanViewHolder(view)

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (position <= plans.size && holder.itemViewType == 0) {
                val plan = plans[position]
                val planViewHolder = holder as PlanViewHolder
                planViewHolder.bind(plan)
            }
        }

        override fun getItemCount(): Int = plans.size
    }


    private fun createMockDate(){
    val date1s = Calendar.getInstance()
    date1s.set(2022,Calendar.JUNE,22)
    val date1e = Calendar.getInstance()
    date1e.set(2022,Calendar.JULY,22)
    val date2s = Calendar.getInstance()
    date2s.set(2022,Calendar.JUNE,27)
    val date2e = Calendar.getInstance()
    date2e.set(2022,Calendar.JULY,10)

    val plans = listOf(
        PlanEntity(UUID.randomUUID(),"Здоровье",date1s.time,date1e.time,0.0,1200.0,
            R.drawable.category_health_icon,""),
        PlanEntity(UUID.randomUUID(),"Еда",date2s.time,date2e.time,0.0,800.0,
            R.drawable.category_food_icon,""))
    for (element in plans) {
        planViewModel.addPlan(element)
    }
}

}