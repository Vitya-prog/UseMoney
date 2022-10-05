package com.android.usemoney.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.ui.add.category.icon

class IconAdapter(var selectedPos:Int = -1):ListAdapter<Int, IconAdapter.IconViewHolder>(DiffCallbackIcon()) {

     inner class IconViewHolder(view: View): RecyclerView.ViewHolder(view){
        val iconImageView: ImageView = itemView as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = ImageView(parent.context)
        view.setBackgroundColor(Color.parseColor("#b3b4fc"))
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.iconImageView.apply {
            setImageResource(getItem(position))
            if (selectedPos == position){
                setBackgroundColor(Color.parseColor("#949494"))
            } else {
                setBackgroundColor(Color.parseColor("#b3b4fc"))
            }
            setOnClickListener {
                icon = getItem(position)
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

private class DiffCallbackIcon : DiffUtil.ItemCallback<Int>(){
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

}