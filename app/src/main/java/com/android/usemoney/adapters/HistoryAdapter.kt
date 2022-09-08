package com.android.usemoney.adapters

import android.content.Context
import android.content.Intent
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
import com.android.usemoney.data.local.Change
import com.android.usemoney.ui.add.AddActivity
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class HistoryAdapter(private val listener:OnItemClickListener)
    : ListAdapter<Change, RecyclerView.ViewHolder>(DiffCallback()) {

    inner class HistoryWithDateViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val accountTextView:TextView = itemView.findViewById(R.id.accountTextView)
        private val valueTextView:TextView = itemView.findViewById(R.id.valueTextView)
        private val categoryImageView:ImageView = itemView.findViewById(R.id.categoryImageView)
        private val dateTextView:TextView = itemView.findViewById(R.id.dateTextView)

        fun bindWithDate(change: Change,context: Context){
            categoryTextView.text = change.name
            val description = change.description
            if (description.length <= 20){
                accountTextView.text = description
            }
            else{
                accountTextView.text = description.substring(0,20) + "..."
            }
            val sign = if (change.type == "Доходы") "+" else "-"
            val color = if(change.type == "Доходы") "#983300" else "#FF0000"
            valueTextView.text = "$sign${(change.value * 100.0).roundToInt() / 100.0}"
            valueTextView.setTextColor(Color.parseColor(color))
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(change.color))
            val drawable = context.resources.getDrawable(change.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            categoryImageView.setImageDrawable(layerDrawable)
            dateTextView.text =  SimpleDateFormat("d MMMM Y").format(change.date.time)
        }
    }
     inner class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val accountTextView: TextView = itemView.findViewById(R.id.accountTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        private val categoryImageView: ImageView = itemView.findViewById(R.id.categoryImageView)

        fun bind(change: Change,context: Context){
            categoryTextView.text = change.name
            val description = change.description
            if (description.length <= 20){
                accountTextView.text = description
            }
            else{
                accountTextView.text = description.substring(0,20) + "..."
            }
            val sign = if (change.type == "Доходы") "+" else "-"
            val color = if(change.type == "Доходы") "#983300" else "#FF0000"
            valueTextView.text = "$sign${(change.value * 100.0).roundToInt() / 100.0}"
            valueTextView.setTextColor(Color.parseColor(color))
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(change.color))
            val drawable = context.resources.getDrawable(change.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            categoryImageView.setImageDrawable(layerDrawable)
        }
    }

    interface OnItemClickListener {
        fun onItemLongClick(change:Change)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType) {
           1-> {
               HistoryWithDateViewHolder(LayoutInflater.from(parent.context)
                   .inflate(R.layout.list_item_history_with_date,parent,false))
           }
           else ->{
               HistoryViewHolder(LayoutInflater.from(parent.context)
                   .inflate(R.layout.list_item_history,parent,false))
           }
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val change = getItem(position)
        val context = holder.itemView.context
        holder.itemView.setOnClickListener{
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("edit","change")
            intent.putExtra("editChange","${change.id}")
            context?.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            listener.onItemLongClick(change)
            true
        }
        if (holder.itemViewType == 0) {
            val historyViewHolder = holder as HistoryAdapter.HistoryViewHolder
            historyViewHolder.bind(change,context)
        }
        if (holder.itemViewType == 1) {
            val historyWithDateViewHolder = holder as HistoryAdapter.HistoryWithDateViewHolder
            historyWithDateViewHolder.bindWithDate(change,context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = 0
        if(position == 0){
            viewType = 1
        } else if (position > 0) {

            if (SimpleDateFormat("d MMMM Y").format(getItem(position).date.time)
                != SimpleDateFormat("d MMMM Y").format(getItem(position-1).date.time)) {
                viewType = 1
            }
        }
        return viewType
    }

}
private class DiffCallback : DiffUtil.ItemCallback<Change>(){
    override fun areItemsTheSame(oldItem: Change, newItem: Change): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Change, newItem: Change): Boolean {
        return oldItem == newItem
    }

}



