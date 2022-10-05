package com.android.usemoney.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.data.local.Plan
import java.text.SimpleDateFormat

class PlanAdapter(private val listener:OnItemClickListener,val context:Context):ListAdapter<Plan,PlanAdapter.PlanViewHolder>(DiffCallBackPlan()) {
    interface OnItemClickListener {
        fun onItemLongClicked(plan: Plan)
    }

    inner class PlanViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val planProgressBar: ProgressBar = itemView.findViewById(R.id.planProgressBar)
        private val startValueTextView: TextView = itemView.findViewById(R.id.startValueTextView)
        private val finishValueTextView: TextView = itemView.findViewById(R.id.finishValueTextView)
        private val planTextView: TextView = itemView.findViewById(R.id.planTextView)
        private val planImageView: ImageView = itemView.findViewById(R.id.planImageView)
        private val datePlanTextView: TextView = itemView.findViewById(R.id.datePlanTextView)

        fun bind(plan: Plan){
            val percent = ((plan.startValue / plan.endValue)*100).toInt()
            planProgressBar.setProgress(percent,true)
            planTextView.text = plan.name
            startValueTextView.text = plan.startValue.toString()
            finishValueTextView.text = plan.endValue.toString()
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(plan.color))
            val drawable = context.resources.getDrawable(plan.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            planImageView.setImageDrawable(layerDrawable)
            datePlanTextView.text = "Осталось ${SimpleDateFormat("d").format(plan.dateSecond.time - plan.dateFirst.time)} дней"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_plan,parent,false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener {
            listener.onItemLongClicked(getItem(position))
            true
        }
          holder.bind(getItem(position))
    }
}

class DiffCallBackPlan: DiffUtil.ItemCallback<Plan>() {
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem == newItem
    }

}
