package com.android.usemoney.ui.add.change

import android.app.DatePickerDialog
import android.graphics.drawable.GradientDrawable
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
import com.android.usemoney.MainActivity
import com.android.usemoney.adapters.CategoryAdapter
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.data.local.Category
import com.android.usemoney.data.local.Change
import com.android.usemoney.databinding.FragmentAddChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject



private const val TAG = "AddChangeFragment"
@AndroidEntryPoint
class AddChangeFragment : Fragment(),CategoryAdapter.OnItemClickListener{
    private lateinit var binding: FragmentAddChangeBinding
    private val addChangeViewModel: AddChangeViewModel by viewModels()
    private var id: String =null.toString()
    private var name: String = null.toString()
    private var type: String = null.toString()
    private var iconChange:Int = 0
    private var color: String = null.toString()
    private var appcode = ""
    private val calendar = Calendar.getInstance()
    @Inject
    lateinit var shape:GradientDrawable
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
                   val d = if (day < 10) "0$day" else "$day"
                   val m = if (month < 10) "0${month + 1}" else "${month + 1}"
                   binding.inputDateEditText.setText("$d.$m.$year")
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
                            type,
                            binding.decriptionEditText.text.toString())
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
                        SimpleDateFormat("dd.MM.yyyy").parse(binding.inputDateEditText.text.toString())!!,
                        type,
                       binding.decriptionEditText.text.toString(),
                        appcode)
                    )
                    AddActivity.closeActivity(activity as AddActivity)
                    MainActivity.startActivity(requireContext())
                }
            }
        }
    }

    private fun loadData(){
        addChangeViewModel.iconCategories.observe(
            viewLifecycleOwner
        ) {
            updateUI(it)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            var change: Change? = null
            if (id != null.toString()) {
                 change = addChangeViewModel.getChange(UUID.fromString(id))
            }
            if (change != null) {
                binding.okeyButton.text = "Обновить"
               binding.inputValueEditText.setText(change.value.toInt().toString())
                binding.decriptionEditText.setText(change.description)
                appcode = change.appcode
               binding.inputDateEditText.setText(SimpleDateFormat("dd.MM.yyyy").format(change.date))

            }
        }


    }
    private fun updateUI(icons:List<Category>){
        val adapter = CategoryAdapter(requireContext(),0,this)
        adapter.submitList(icons)
        binding. inputCategoryRecyclerView.adapter = adapter
    }
companion object {

    fun newInstance(id:String):AddChangeFragment{
        val f = AddChangeFragment()
        f.id = id
        return f
        }
    }

    override fun onItemClick(category: Category) {
        name = category.name
        type = category.type
        iconChange = resources.getIdentifier(category.icon,"drawable",activity?.packageName)
        color = category.color
    }
}