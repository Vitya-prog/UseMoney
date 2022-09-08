package com.android.usemoney.ui.add.plan

import android.app.DatePickerDialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.usemoney.MainActivity
import com.android.usemoney.adapters.CategoryAdapter
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Plan
import com.android.usemoney.databinding.FragmentAddPlanBinding
import com.android.usemoney.ui.add.AddActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddPlanFragment : Fragment(),CategoryAdapter.OnItemClickListener {
    private lateinit var binding:FragmentAddPlanBinding
    private var name:String = ""
    private var icon:Int = 0
    private var color:String =""
    private val addPlanViewModel:AddPlanViewModel by viewModels()
    private val calendarF = Calendar.getInstance()
    private val calendarS = Calendar.getInstance()
    @Inject
    lateinit var shape:GradientDrawable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlanBinding.inflate(inflater,container,false)
        loadIcons()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.firstDate.setOnClickListener {
            val initialYear = calendarF.get(Calendar.YEAR)
            val initialMonth = calendarF.get(Calendar.MONTH)
            val initialDay = calendarF.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    val d = if (day < 10) "0$day" else "$day"
                    val m = if (month < 10) "0${month + 1}" else "${month + 1}"
                    binding.firstDate.setText("$d.$m.$year")
                    calendarF.set(year,month,day)
                },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        binding.secondDate.setOnClickListener {
            val initialYear = calendarS.get(Calendar.YEAR)
            val initialMonth = calendarS.get(Calendar.MONTH)
            val initialDay = calendarS.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    val d = if (day < 10) "0$day" else "$day"
                    val m = if (month < 10) "0${month + 1}" else "${month + 1}"
                    binding.secondDate.setText("$d.$m.$year")
                    calendarS.set(year,month,day)
                },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        binding.okButton.setOnClickListener {
            addPlanViewModel.addPlan(
                Plan(
                UUID.randomUUID(),
                name,
                calendarF.time,
                calendarS.time,
                0.0,
                    binding.budgetEditText.text.toString().toDouble(),
                icon,
                color)
            )
            AddActivity.closeActivity(activity as AddActivity)
            MainActivity.startActivity(requireContext())
        }
    }

    private fun loadIcons(){
       addPlanViewModel.iconCategories.observe(
           viewLifecycleOwner
       ) {
         updateUI(it)
       }
    }
    private fun updateUI(icons:List<Category>){
        binding.categoryRecyclerView.layoutManager = GridLayoutManager(context,5)
        val adapter = CategoryAdapter(requireContext(),-1,this)
        adapter.submitList(icons)
        binding.categoryRecyclerView.adapter = adapter
    }
    override fun onItemClick(category: Category) {
        name = category.name
        icon = resources.getIdentifier(category.icon,"drawable",activity?.packageName)
        color = category.color
    }

}