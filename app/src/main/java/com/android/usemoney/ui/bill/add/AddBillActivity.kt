package com.android.usemoney.ui.bill.add


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.usemoney.R
import com.android.usemoney.databinding.ActivityAddBillBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBillActivity : AppCompatActivity() {
    private var nameBank= arrayOf("ПриватБанк","Monobank")
    private var iconBank=arrayOf(R.drawable.privatbank,R.drawable.monobank)
    private var adapter = AddBillAdapter(emptyArray(), emptyArray())

    private inner class AddBillAdapter(val name: Array<String>, val icon: Array<Int>):RecyclerView.Adapter<AddBillHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddBillHolder {
            return AddBillHolder(layoutInflater.inflate(R.layout.list_item_bill,parent,false))
        }

        override fun onBindViewHolder(holder: AddBillHolder, position: Int) {
            holder.bind(name[position],icon[position])
            holder.itemView.setOnClickListener {
                when (position){
                    0->switchFragment(PrivatBankFragment())
                   // 1->switchFragment()
                }
            }
        }

        private fun switchFragment(fragment: Fragment) {
            binding.bankRecycleView.isInvisible = true
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.add_bill,fragment)
                .addToBackStack(null)
                .commit()
        }

        override fun getItemCount(): Int = name.size

    }

   private inner class AddBillHolder(view: View):RecyclerView.ViewHolder(view) {

       private val billImageView: ImageView = itemView.findViewById(R.id.billImageView)
       private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
       private val valueBillTextView: TextView = itemView.findViewById(R.id.valueBillTextView)

       fun bind(name: String, icon: Int) {
            billImageView.setImageResource(icon)
            nameTextView.text = name
            valueBillTextView.isInvisible = true
       }

   }

    private lateinit var binding:ActivityAddBillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillBinding.inflate(layoutInflater)
        binding.bankRecycleView.layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)
        updateUI(nameBank,iconBank)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.bankRecycleView.isInvisible = false
    }
    private fun updateUI(name:Array<String>,icon:Array<Int>) {
        adapter = AddBillAdapter(name,icon)
        binding.bankRecycleView.adapter = adapter
    }
}