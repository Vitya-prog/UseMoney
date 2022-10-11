package com.android.usemoney.ui.add.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.adapters.IconAdapter
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.data.local.Category
import com.android.usemoney.databinding.FragmentAddCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "AddCategoryFragment"
var icon: Int = -1
@AndroidEntryPoint
class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private var categoryId:String? = null
    private var adapter = IconAdapter()
    private var data = listOf(R.drawable.cafe_icon,R.drawable.health_icon,R.drawable.food_icon
                                ,R.drawable.debt_icon,R.drawable.deposite_icon,
                            R.drawable.family_icon,R.drawable.gift_icon,
                            R.drawable.salary_icon,R.drawable.transport_icon,R.drawable.unknown_icon)
    private val addCategoryViewModel: AddCategoryViewModel by viewModels()
    private lateinit var category: Category
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(inflater,container,false)
        binding.inputIconRecyclerView.layoutManager = GridLayoutManager(context,5)
        loadIcon()
        adapter = IconAdapter()
        adapter.submitList(data)
        binding.inputIconRecyclerView.adapter = adapter
            if (categoryId != null) {
                addCategoryViewModel.loadCategory(UUID.fromString(categoryId))
            }
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCategoryViewModel.category.observe(
            viewLifecycleOwner
        ){category ->
            category?.let {
                this.category = category
            }
            updateUI(category)
        }
    }

    private fun updateUI(category: Category) {
        binding.inputNameCategory.setText(category.name)
        if (category.type == "Доходы"){
            binding.incomeRadioButton.isChecked = true
        } else {
            binding.costRadioButton.isChecked = true
        }
    }


    private fun loadIcon() {
    for (i in 1..28){
        val icon = resources.getIdentifier("icon$i","drawable",context?.packageName)
        data = data + icon
    }
}


    override fun onStart() {
        super.onStart()
        binding.okButton.setOnClickListener{
            if (binding.inputNameCategory.text.isEmpty()) {
                Toast.makeText(context,"Введите имя!",10).show()
            } else if (!(binding.incomeRadioButton.isChecked || binding.costRadioButton.isChecked)){
                Toast.makeText(context,"Выберите тип категории!",10).show()
            } else if (icon == -1) {
                Toast.makeText(context,"Выберите иконку!",10).show()
            } else if (categoryId == null){
                val type = if(binding.incomeRadioButton.isChecked) "Доходы" else "Расходы"
                addCategoryViewModel.addCategory(
                    Category(UUID.randomUUID(),
                        binding.inputNameCategory.text.toString(),
                        type,0.0,
                        icon.toString(),
                        getRandomColor())
                )
               AddActivity.closeActivity(activity as AddActivity)
               MainActivity.startActivity(requireContext())
            } else {
                val type = if(binding.incomeRadioButton.isChecked) "Доходы" else "Расходы"
                addCategoryViewModel.updateCategory(
                    Category(UUID.fromString(categoryId),
                        binding.inputNameCategory.text.toString(),
                        type,
                        0.0,
                        icon.toString(),
                        getRandomColor())
                )
                AddActivity.closeActivity(activity as AddActivity)
                MainActivity.startActivity(requireContext())
            }
        }
        binding.cancelButton.setOnClickListener {
            AddActivity.closeActivity(activity as AddActivity)
        }
    }

    override fun onStop() {
        super.onStop()
        category.name = binding.inputNameCategory.text.toString()
        category.icon = icon.toString()
        category.color = getRandomColor()
        addCategoryViewModel.updateCategory(category)
    }
    private fun getRandomColor(): String {
        val random = Random()
        val letters = "0123456789ABCDEF"
        var color = "#"
        color += letters[(random.nextFloat() * 15).roundToInt()]
        color += letters[(random.nextFloat() * 15).roundToInt()]
        color += letters[(random.nextFloat() * 15).roundToInt()]
        color += letters[(random.nextFloat() * 15).roundToInt()]
        color += letters[(random.nextFloat() * 15).roundToInt()]
        color += letters[(random.nextFloat() * 15).roundToInt()]
        Log.d(TAG, color)
        return color
    }
    companion object {
        fun newInstance(id:String):AddCategoryFragment{
            val fragment = AddCategoryFragment()
            fragment.categoryId = id
            return fragment
        }
    }
}