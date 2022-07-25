package com.android.usemoney.ui.history

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.add_data.AddActivity
import com.android.usemoney.entities.ChangeEntity
import java.text.SimpleDateFormat

private const val TAG = "HistoryFragment"
class HistoryFragment : Fragment() {
private lateinit var recycleViewHistory: RecyclerView
private lateinit var addButton: Button
private var adapter = HistoryViewAdapter(emptyList())
 private val historyViewModel: HistoryViewModel by lazy {
HistoryViewModel().also {
    ViewModelProvider(this)[HistoryViewModel::class.java]
}
 }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_history, container, false)
//       createMockDate()
        recycleViewHistory = view.findViewById(R.id.recycleViewHistory)
        recycleViewHistory.layoutManager = LinearLayoutManager(context)
        recycleViewHistory.adapter = adapter
        addButton = view.findViewById(R.id.addChangeButton)
        addButton.setOnClickListener{
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","change")
            context?.startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyViewModel.historyListLiveData.observe(
            viewLifecycleOwner
        ) { changes ->
            updateUI(changes)
        }
    }
    private fun updateUI(changeEntity: List<ChangeEntity>){
         adapter = HistoryViewAdapter(changeEntity.sortedByDescending { it.date })
        recycleViewHistory.adapter = adapter
    }
    private inner class HistoryViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val accountTextView:TextView = itemView.findViewById(R.id.accountTextView)
        private val valueTextView:TextView = itemView.findViewById(R.id.valueTextView)
        private val categoryImageView:ImageView = itemView.findViewById(R.id.categoryImageView)

        fun bind(change:ChangeEntity){
            categoryTextView.text = change.name
            accountTextView.text = "Наличные"
            var s = "-"
            var color = "#FF0000"
            if (change.type == "Доходы"){
                s = "+"
                color = "#983300"
            }
            valueTextView.text = "$s${change.value.toInt()}"
            valueTextView.setTextColor(Color.parseColor(color))
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(change.color))
            val drawable = resources.getDrawable(change.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            categoryImageView.setImageDrawable(layerDrawable)
        }


    }
    private inner class HistoryWithDateViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val accountTextView:TextView = itemView.findViewById(R.id.accountTextView)
        private val valueTextView:TextView = itemView.findViewById(R.id.valueTextView)
        private val categoryImageView:ImageView = itemView.findViewById(R.id.categoryImageView)
        private val dateTextView:TextView = itemView.findViewById(R.id.dateTextView)

        fun bindWithDate(change: ChangeEntity){
            categoryTextView.text = change.name
            accountTextView.text = "Наличные"
            var s = "-"
            var color = "#FF0000"
            if (change.type == "Доходы"){
                s = "+"
                color = "#983300"
            }
            valueTextView.text = "$s${change.value.toInt()}"
            valueTextView.setTextColor(Color.parseColor(color))
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 50f
            shape.setColor(Color.parseColor(change.color))
            val drawable = resources.getDrawable(change.icon)
            val layerDrawable = LayerDrawable(arrayOf(shape,drawable))
            categoryImageView.setImageDrawable(layerDrawable)
            dateTextView.text =  SimpleDateFormat("d MMMM Y").format(change.date.time)
        }
    }
    private inner class HistoryViewAdapter(var changeEntity: List<ChangeEntity>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                1 -> {
                    HistoryWithDateViewHolder(layoutInflater.inflate(R.layout.list_item_history_with_date,parent,false))
                }
                else -> {
                    HistoryViewHolder(layoutInflater.inflate(R.layout.list_item_history,parent,false))
                }
            }

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val change = changeEntity[position]
                holder.itemView.setOnClickListener{
                    val intent = Intent(context, AddActivity::class.java)
                    intent.putExtra("add","change")
                    context?.startActivity(intent)
                }
                if (holder.itemViewType == 0) {
                    val historyViewHolder = holder as HistoryViewHolder
                    historyViewHolder.bind(change)
                }
                if (holder.itemViewType == 1) {
                    val historyWithDateViewHolder = holder as HistoryWithDateViewHolder
                    historyWithDateViewHolder.bindWithDate(change)
                }
            }

        override fun getItemCount(): Int = changeEntity.size

        override fun getItemViewType(position: Int): Int {
            var viewType = 0
            if(position == 0){
                viewType = 1
            } else if (position > 0) {

                if (SimpleDateFormat("d MMMM Y").format(changeEntity[position].date.time)
                    != SimpleDateFormat("d MMMM Y").format(changeEntity[position-1].date.time)) {
                    viewType = 1
                }
            }
            return viewType
        }

    }
}


