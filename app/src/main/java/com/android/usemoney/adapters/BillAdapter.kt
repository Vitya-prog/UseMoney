package com.android.usemoney.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.data.local.Bill
import java.util.*
import kotlin.math.roundToInt

class BillAdapter(val context: Context,private val listener:OnBillLongListener): ListAdapter<Bill, BillAdapter.BillViewHolder>(DiffCallBackBill()) {
    inner class BillViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val billImageView: ImageView = itemView.findViewById(R.id.billImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val valueBillTextView: TextView = itemView.findViewById(R.id.valueBillTextView)

        fun bind(bill: Bill){
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(getRandomColor()))
            val drawable = context.resources.getDrawable(R.drawable.card_icon)
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

    interface OnBillLongListener {
        fun deleteBill(bill:Bill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_bill,parent,false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener {
            listener.deleteBill(getItem(position))
            true
        }
        holder.bind(getItem(position))
    }
}

class DiffCallBackBill: DiffUtil.ItemCallback<Bill>() {
    override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
        return oldItem == newItem
    }

}
