package com.android.usemoney.ui.add.category




import android.graphics.Color
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
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.data.model.Category
import com.android.usemoney.databinding.FragmentAddCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "AddCategoryFragment"
@AndroidEntryPoint
class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private var categoryId:String? = null
    private var icon: Int = -1
    private var adapter = InputIconViewAdapter(emptyList())
    private var data = listOf(R.drawable.cafe_icon,R.drawable.health_icon,R.drawable.food_icon
                                ,R.drawable.debt_icon,R.drawable.deposite_icon,
                            R.drawable.family_icon,R.drawable.gift_icon,
                            R.drawable.salary_icon,R.drawable.transport_icon)
    private val addCategoryViewModel: AddCategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(inflater,container,false)
        binding.inputIconRecyclerView.layoutManager = GridLayoutManager(context,5)
        loadIcon()
        adapter = InputIconViewAdapter(data)
        binding.inputIconRecyclerView.adapter = adapter
            if (categoryId != null){
                loadCategory()
                binding.okButton.text = "Обновить"
            }

       return binding.root
    }

    private fun loadCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            val category = addCategoryViewModel.getCategory(UUID.fromString(categoryId))
            binding.inputNameCategory.setText(category.name)
            if (category.type == "Доходы"){
                binding.incomeRadioButton.isChecked = true
            } else {
                binding.costRadioButton.isChecked = true
            }
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
                        icon,
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
                        icon,
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
    private inner class InputIconViewHolder(view:View):RecyclerView.ViewHolder(view){
         val iconImageView:ImageView = itemView as ImageView
    }
    private inner class InputIconViewAdapter(val dataList:List<Int>,var selectedPos:Int = -1):RecyclerView.Adapter<InputIconViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputIconViewHolder {
            val view = ImageView(parent.context)
            view.setBackgroundColor(Color.parseColor("#b3b4fc"))
            return InputIconViewHolder(view)
        }

        override fun onBindViewHolder(holder: InputIconViewHolder, position: Int) {
            holder.iconImageView.apply {
                setImageResource(dataList[position])
                if (selectedPos == position){
                    holder.iconImageView.setBackgroundColor(Color.parseColor("#949494"))
                } else {
                    holder.iconImageView.setBackgroundColor(Color.parseColor("#b3b4fc"))
                }
            }
            holder.iconImageView.setOnClickListener {
                 icon = dataList[position]
                setSingleSelection(holder.adapterPosition)
            }
        }
        private fun setSingleSelection(adapterPosition: Int){
            if (adapterPosition == RecyclerView.NO_POSITION) return
            notifyItemChanged(selectedPos)
            selectedPos = adapterPosition
            notifyItemChanged(selectedPos)

        }

        override fun getItemCount(): Int = dataList.size
    }
    companion object {
        fun newInstance(id:String):AddCategoryFragment{
            val fragment = AddCategoryFragment()
            fragment.categoryId = id
            return fragment
        }
    }
}