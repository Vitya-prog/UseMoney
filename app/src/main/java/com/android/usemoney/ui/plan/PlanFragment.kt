package com.android.usemoney.ui.plan

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.usemoney.adapters.PlanAdapter
import com.android.usemoney.data.local.Plan
import com.android.usemoney.databinding.FragmentPlanBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.android.usemoney.ui.add.AddActivity as AddActivity

private const val TAG ="PlanFragment"
@AndroidEntryPoint
class PlanFragment : Fragment(),PlanAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPlanBinding
    private val planViewModel: PlanViewModel by viewModels()
    @Inject
    lateinit var shape:GradientDrawable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanBinding.inflate(inflater, container, false)
        binding.planRecycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPlan()
    }

    override fun onStart() {
        super.onStart()
        binding.addPlanButton.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("add","plan")
            context?.startActivity(intent)
        }
    }
    private fun loadPlan() {
      planViewModel.plans.observe(
          viewLifecycleOwner
      ){
          updateUI(it)
      }

    }
    private fun updateUI(plans:List<Plan>){
    val adapter = PlanAdapter(this,requireContext())
    adapter.submitList(plans)
    binding.planRecycleView.adapter = adapter

}


    override fun onItemLongClicked(plan: Plan) {
            planViewModel.deletePlan(plan)
    }

}