package com.android.usemoney.ui.bill

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.data.model.Bill
import com.android.usemoney.databinding.FragmentCardBinding
import com.android.usemoney.ui.bill.add.AddBillActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class BillFragment : Fragment() {
    private val billViewModel: BillViewModel by viewModels()
    private lateinit var binding:FragmentCardBinding
    private var adapter = BillAdapter(emptyList())

    private inner class BillHolder(view:View):RecyclerView.ViewHolder(view){
        private val billImageView:ImageView = itemView.findViewById(R.id.billImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val valueBillTextView:TextView = itemView.findViewById(R.id.valueBillTextView)

         fun bind(bill: Bill){
             val shape = GradientDrawable()
             shape.shape = GradientDrawable.RECTANGLE
             shape.cornerRadius = 50f
             shape.setColor(Color.parseColor(getRandomColor()))

             val drawable = resources.getDrawable(R.drawable.card_icon)
             val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
             billImageView.setImageDrawable(layerDrawable)
             nameTextView.text = bill.name
             valueBillTextView.text = bill.value.toInt().toString()

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
            return color
        }
    }

    private inner class BillAdapter(val bills: List<Bill>):RecyclerView.Adapter<BillHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillHolder {
            val view = layoutInflater.inflate(R.layout.list_item_bill,parent,false)
            return BillHolder(view)
        }

        override fun onBindViewHolder(holder: BillHolder, position: Int) {
            holder.bind(bills[position])
        }

        override fun getItemCount(): Int = bills.size

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater,container,false)
        binding.billRecyclerView.layoutManager = LinearLayoutManager(context)
       loadData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.addBillButton.setOnClickListener {
            val intent = Intent(context, AddBillActivity::class.java)
            intent.putExtra("add","change")
            context?.startActivity(intent)
        }
    }
    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch{
            val bills = billViewModel.getBills()
            createBill(bills)
            updateUI(bills)
        }
    }

    private fun updateUI(bills: List<Bill>) {
        adapter = BillAdapter(bills)
        binding.billRecyclerView.adapter = adapter
    }

    private fun createBill(bills:List<Bill>) {
        if (bills.isEmpty()){
            billViewModel.addBill(Bill(UUID.randomUUID(),"Наличные",0.0))
            billViewModel.addBill(Bill(UUID.randomUUID(),"Карта ПриватБанк",0.0))
        }
    }
}