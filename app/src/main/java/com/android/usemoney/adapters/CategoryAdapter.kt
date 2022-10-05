package com.android.usemoney.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.data.local.Category

class CategoryAdapter(val context: Context, private var selectedPos: Int = -1,private val listener:OnItemClickListener)
    : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallBackCategory()) {
    interface OnItemClickListener{
        fun onItemClick(category: Category)
    }
    inner class CategoryViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val iconButton: Button = itemView as Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = Button(parent.context,null, R.style.categoryButton)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 50f
        shape.setColor(Color.parseColor(getItem(position).color))
        val drawable = context.resources.getDrawable(context.resources
            .getIdentifier(getItem(position).icon,"drawable",context.packageName))
        val layerDrawable = LayerDrawable(arrayOf(shape, drawable))
        holder.iconButton.apply {
            if (selectedPos == position) {
                listener.onItemClick(getItem(position))
                holder.iconButton.textSize = 16f
            } else {
                holder.iconButton.textSize = 12f
            }
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setCompoundDrawablesWithIntrinsicBounds(null, layerDrawable, null, null)
            text = getItem(position).name
            setOnClickListener {
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
}

class DiffCallBackCategory:DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}
