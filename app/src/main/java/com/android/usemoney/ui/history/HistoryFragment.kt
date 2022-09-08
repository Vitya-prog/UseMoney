package com.android.usemoney.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.usemoney.adapters.HistoryAdapter
import com.android.usemoney.data.local.Change
import com.android.usemoney.ui.add.AddActivity
import com.android.usemoney.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HistoryFragment"
@AndroidEntryPoint
class HistoryFragment : Fragment(),HistoryAdapter.OnItemClickListener {
private lateinit var binding: FragmentHistoryBinding
 private val historyViewModel: HistoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        binding.recycleViewHistory.layoutManager = LinearLayoutManager(context)
        binding.addChangeButton.setOnClickListener{
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","change")
            context?.startActivity(intent)
        }
        Log.d(TAG,"Start history")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HistoryAdapter(this)
        historyViewModel.historyListLiveData.observe(
            viewLifecycleOwner
        ) { changes ->
            Log.d(TAG,"$changes")
            adapter.submitList(changes.sortedByDescending { it.date })
        }
        binding.recycleViewHistory.adapter = adapter
    }

    override fun onItemLongClick(change: Change) {
        historyViewModel.deleteChange(change)
    }

}


