package com.android.usemoney.ui.add.change

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.data.model.Category
import com.android.usemoney.data.model.Change
import com.android.usemoney.databinding.FragmentAddChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "AddChangeFragment"
@AndroidEntryPoint
class AddChangeFragment : Fragment(){
    private lateinit var binding: FragmentAddChangeBinding
    private val addChangeViewModel: AddChangeViewModel by viewModels()
    private var adapter = AddChangeAdapter(emptyList(),0)
    private var id: String =null.toString()
    private var name: String = null.toString()
    private var type: String = null.toString()
    private var iconChange:Int = 0
    private var color: String = null.toString()
    private val calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChangeBinding.inflate(inflater,container,false)
        binding.inputCategoryRecyclerView.layoutManager = GridLayoutManager(context,5)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    override fun onStart() {
        super.onStart()
        binding.inputDateEditText.setOnClickListener {
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
           val dialogFragment = DatePickerDialog(requireContext(),
               { view: DatePicker, year: Int, month: Int, day: Int ->
                   binding.inputDateEditText.setText("$day/$month/$year")
                    calendar.set(year,month,day)
               },initialYear,initialMonth,initialDay)
            dialogFragment.show()
        }
        binding.okeyButton.setOnClickListener{
            when {
                binding.inputValueEditText.text.isEmpty()->Toast.makeText(requireContext(),"Введите значение!",10)
                binding.inputDateEditText.text.isEmpty() -> Toast.makeText(requireContext(),"Выберите дату!",10)
                name.isEmpty() ->Toast.makeText(requireContext(),"Выберите категорию!",10)
                id == null.toString() -> {
                    addChangeViewModel.addChange(
                        Change(
                            UUID.randomUUID(),
                            name,
                            binding.inputValueEditText.text.toString().toDouble(),
                            iconChange,
                            color,
                            calendar.time,
                            type)
                    )
                    AddActivity.closeActivity(activity as AddActivity)
                    MainActivity.startActivity(requireContext())
                }
                id!=null.toString()->{
                    addChangeViewModel.updateChange(
                        Change(
                        UUID.fromString(id),
                        name,
                        binding.inputValueEditText.text.toString().toDouble(),
                        iconChange,
                        color,
                        calendar.time,
                        type)
                    )
                    AddActivity.closeActivity(activity as AddActivity)
                    MainActivity.startActivity(requireContext())
                }
            }
        }
    }

    private fun loadData(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val icons = addChangeViewModel.getIconCategories()
            var change: Change? = null
            if (id != null.toString()) {
                 change = addChangeViewModel.getChange(UUID.fromString(id))
            }
            if (change != null) {
               binding.inputValueEditText.setText(change.value.toInt().toString())
               binding.okeyButton.text = "Обновить"
               binding.inputDateEditText.setText(change.date.toString())

            }
            updateUI(icons)
        }
    }
    private fun updateUI(icons:List<Category>){
        adapter = AddChangeAdapter(icons,0)
        binding. inputCategoryRecyclerView.adapter = adapter
    }
    private inner class AddChangeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val iconButton: Button = itemView as Button
    }
    private inner class AddChangeAdapter(val icons:List<Category>, var selectedPos:Int = -1):RecyclerView.Adapter<AddChangeViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddChangeViewHolder {
            val view = Button(parent.context,null, R.style.categoryButton)
            return AddChangeViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddChangeViewHolder, position: Int) {
            if (icons[position].name != "Неизвестен") {
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.cornerRadius = 50f
                shape.setColor(Color.parseColor(icons[position].color))
                val drawable = resources.getDrawable(icons[position].icon)
                val layerDrawable = LayerDrawable(arrayOf(shape, drawable))
                holder.iconButton.apply {
                    if (selectedPos == position) {
                        type = icons[position].type
                        name = icons[position].name
                        iconChange = icons[position].icon
                        color = icons[position].color
                        holder.iconButton.textSize = 16f
                    } else {
                        holder.iconButton.textSize = 12f
                    }
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setCompoundDrawablesWithIntrinsicBounds(null, layerDrawable, null, null)
                    text = icons[position].name
                    setOnClickListener {
                        setSingleSelection(holder.adapterPosition)
                    }
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
companion object {

    fun newInstance(id:String):AddChangeFragment{
        val f = AddChangeFragment()
        f.id = id
        return f
        }
    }
}