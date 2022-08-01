package com.android.usemoney.ui.add.plan

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Plan
import com.android.usemoney.ui.add.AddActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddPlanFragment : Fragment() {
    private lateinit var categoryRecycleView: RecyclerView
    private lateinit var firstDate: EditText
    private lateinit var secondDate:EditText
    private lateinit var budgetEditText: EditText
    private lateinit var okButton: Button
    private var name:String = ""
    private var icon:Int = 0
    private var color:String =""
    private val addPlanViewModel:AddPlanViewModel by viewModels()
    private val calendarF = Calendar.getInstance()
    private val calendarS = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_plan, container, false)
        categoryRecycleView = view.findViewById(R.id.categoryRecyclerView)
        firstDate = view.findViewById(R.id.firstDate)
        secondDate = view.findViewById(R.id.secondDate)
        budgetEditText = view.findViewById(R.id.budgetEditText)
        okButton = view.findViewById(R.id.okButton)
        loadIcons()
        return view
    }

    override fun onStart() {
        super.onStart()
        firstDate.setOnClickListener {
            val initialYear = calendarF.get(Calendar.YEAR)
            val initialMonth = calendarF.get(Calendar.MONTH)
            val initialDay = calendarF.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { view: DatePicker, year: Int, month: Int, day: Int ->
                    firstDate.setText("$day/$month/$year")
                    calendarF.set(year,month,day)
                },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        secondDate.setOnClickListener {
            val initialYear = calendarS.get(Calendar.YEAR)
            val initialMonth = calendarS.get(Calendar.MONTH)
            val initialDay = calendarS.get(Calendar.DAY_OF_MONTH)
            val dialogFragment = DatePickerDialog(requireContext(),
                { view: DatePicker, year: Int, month: Int, day: Int ->
                    secondDate.setText("$day/$month/$year")
                    calendarS.set(year,month,day)
                },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        okButton.setOnClickListener {
            addPlanViewModel.addPlan(
                Plan(
                UUID.randomUUID(),
                name,
                calendarF.time,
                calendarS.time,
                0.0,
                budgetEditText.text.toString().toDouble(),
                icon,
                color)
            )
            AddActivity.closeActivity(activity as AddActivity)
            MainActivity.startActivity(requireContext())
        }
    }

    private fun loadIcons(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            val icons = addPlanViewModel.getIconCategories()
            updateUI(icons)
        }
    }
    private fun updateUI(icons:List<Category>){
        categoryRecycleView.layoutManager = GridLayoutManager(context,5)
        categoryRecycleView.adapter = CategoryAdapter(icons)
    }

    private inner class CategoryViewHolder(view:View):RecyclerView.ViewHolder(view){
    var button = itemView as Button
    }
    private inner class CategoryAdapter(val icons: List<Category>, var selectedPos:Int = -1):
        RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val view = Button(context,null, R.style.categoryButton)
            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(icons[position].color))
            val drawable = resources.getDrawable(icons[position].icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            holder.button.apply {
                if (selectedPos == position){
                    holder.button.textSize = 16f
                } else {
                    holder.button.textSize = 12f
                }
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setCompoundDrawablesWithIntrinsicBounds(null, layerDrawable, null, null)
                text = icons[position].name
                setOnClickListener {
                    name = icons[position].name
                    icon = icons[position].icon
                    color = icons[position].color
                    setSingleSelection(holder.adapterPosition)
                }
            }
        }
        private fun setSingleSelection(adapterPosition: Int){
            if (adapterPosition == RecyclerView.NO_POSITION) return
            notifyItemChanged(selectedPos)
            selectedPos = adapterPosition
            notifyItemChanged(selectedPos)

        }
        override fun getItemCount(): Int = icons.size
    }

}