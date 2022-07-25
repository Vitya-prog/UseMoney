package com.android.usemoney.add_data.change

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.add_data.AddActivity
import com.android.usemoney.entities.CategoryEntity
import com.android.usemoney.entities.ChangeEntity
import com.android.usemoney.repository.UseMoneyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "AddChangeFragment"
class AddChangeFragment : Fragment(){
    private lateinit var inputCategoryRecyclerView: RecyclerView
    private lateinit var inputValueEditText: EditText
    private lateinit var okeyButton:Button
    private lateinit var inputDateText: EditText
    private val useMoneyRepository = UseMoneyRepository.get()
    private var adapter = AddChangeAdapter(emptyList(),0)
    private var name: String? = null
    private var type: String = null.toString()
    private var iconChange:Int = 0
    private var color: String = ""
    private val calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_change, container, false)
        inputCategoryRecyclerView = view.findViewById(R.id.inputCategoryRecyclerView)
        inputCategoryRecyclerView.layoutManager = GridLayoutManager(context,5)
        inputValueEditText = view.findViewById(R.id.inputValueEditText)
        okeyButton = view.findViewById(R.id.okeyButton)
        inputDateText = view.findViewById(R.id.inputDateEditText)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadIcons()
    }

    override fun onStart() {
        super.onStart()
        inputDateText.setOnClickListener {
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
           val dialogFragment = DatePickerDialog(requireContext(),
               { view: DatePicker, year: Int, month: Int, day: Int ->
                   inputDateText.setText("$day/$month/$year")
                    calendar.set(year,month,day)
               },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        okeyButton.setOnClickListener{
            when {
               inputValueEditText.text.isEmpty()->Toast.makeText(requireContext(),"Введите значение!",10)
                inputDateText.text.isEmpty() -> Toast.makeText(requireContext(),"Выберите дату!",10)
                name?.isEmpty() == true ->Toast.makeText(requireContext(),"Выберите категорию!",10)
                else -> {
                    useMoneyRepository.addChanges(
                        ChangeEntity(
                            UUID.randomUUID(),
                            name.toString(),
                            inputValueEditText.text.toString().toDouble(),
                            iconChange,
                            color,
                            calendar.time,
                            type
                        )
                    )
                    AddActivity.closeActivity(activity as AddActivity)
                }
            }
        }
    }
    private suspend fun getIconCategories():List<CategoryEntity>{
        val iconList = CoroutineScope(Dispatchers.IO).async {
            useMoneyRepository.getIconCategories()
        }
        return iconList.await()
    }
    private fun loadIcons(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            val icons = getIconCategories()
            updateUI(icons)
        }
    }
    private fun updateUI(icons:List<CategoryEntity>){
        adapter = AddChangeAdapter(icons,0)
        inputCategoryRecyclerView.adapter = adapter
    }
    private inner class AddChangeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val iconButton: Button = itemView as Button
    }
    private inner class AddChangeAdapter(val icons:List<CategoryEntity>,var selectedPos:Int = -1):RecyclerView.Adapter<AddChangeViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddChangeViewHolder {
            val view = Button(parent.context,null, R.style.categoryButton)
            return AddChangeViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddChangeViewHolder, position: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(icons[position].color))
            val drawable = resources.getDrawable(icons[position].icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            holder.iconButton.apply {
                if (selectedPos == position){
                    holder.iconButton.textSize = 16f
                } else {
                    holder.iconButton.textSize = 12f
                }
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setCompoundDrawablesWithIntrinsicBounds(null, layerDrawable, null, null)
                text = icons[position].name
                setOnClickListener {
                    type = icons[position].type
                    name = icons[position].name
                    iconChange = icons[position].icon
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