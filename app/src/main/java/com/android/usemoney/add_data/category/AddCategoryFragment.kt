package com.android.usemoney.add_data.category




import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.MainActivity
import com.android.usemoney.R
import com.android.usemoney.add_data.AddActivity
import com.android.usemoney.entities.CategoryEntity
import java.util.*

private const val TAG = "AddCategoryFragment"
class AddCategoryFragment : Fragment() {

    private lateinit var inputNameCategory:EditText
    private lateinit var inputIconRecyclerView: RecyclerView
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var typeRadioGroup: RadioGroup
    private lateinit var incomeRadioButton: RadioButton
    private lateinit var costRadioButton: RadioButton
    private var icon: Int = -1
    private var adapter = InputIconViewAdapter(emptyList())
    private var data = listOf(R.drawable.cafe_icon,R.drawable.health_icon,R.drawable.food_icon
                                ,R.drawable.debt_icon,R.drawable.deposite_icon,
                            R.drawable.family_icon,R.drawable.gift_icon,
                            R.drawable.salary_icon,R.drawable.transport_icon)
    private val addCategoryViewModel: AddCategoryViewModel by lazy {
        AddCategoryViewModel().also {
            ViewModelProvider(this)[AddCategoryViewModel::class.java]
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_add_category, container, false)
       inputNameCategory = view.findViewById(R.id.inputNameCategory)
       inputIconRecyclerView = view.findViewById(R.id.inputIconRecyclerView)
       okButton = view.findViewById(R.id.okButton)
       cancelButton = view.findViewById(R.id.cancelButton)
       typeRadioGroup = view.findViewById(R.id.typeRadioGroup)
       incomeRadioButton = view.findViewById(R.id.incomeRadioButton)
       costRadioButton = view.findViewById(R.id.costRadioButton)


       return view
    }
private fun loadIcon() {
    for (i in 1..28){
        val icon = resources.getIdentifier("icon$i","drawable",context?.packageName)
        data = data + icon
    }
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputIconRecyclerView.layoutManager = GridLayoutManager(context,5)
        loadIcon()
        adapter = InputIconViewAdapter(data)
        inputIconRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        inputIconRecyclerView.setOnClickListener {

        }
        okButton.setOnClickListener{

            if (inputNameCategory.text.isEmpty()) {
                Toast.makeText(context,"Введите имя!",10).show()
            } else if (!(incomeRadioButton.isChecked || costRadioButton.isChecked)){
                Toast.makeText(context,"Выберите тип категории!",10).show()
            } else if (icon == -1) {
                Toast.makeText(context,"Выберите иконку!",10).show()
            } else {
                val type = if(incomeRadioButton.isChecked) "Доходы" else "Расходы"
                addCategoryViewModel.addCategory(CategoryEntity(UUID.randomUUID(),
                    inputNameCategory.text.toString(),type,0.0,
                   icon, getRandomColor()))
               AddActivity.closeActivity(activity as AddActivity)
               MainActivity.startActivity(requireContext())
            }
        }
        cancelButton.setOnClickListener {
            AddActivity.closeActivity(activity as AddActivity)
        }
    }
    private fun getRandomColor(): String {
        val random = Random()
        val letters = "0123456789ABCDEF".split("".toRegex()).toTypedArray()
        var color = "#"
        for (i in 0..5) {
            color += letters[Math.round(random.nextFloat() * 15)]
        }
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
}