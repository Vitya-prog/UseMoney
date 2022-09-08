package com.android.usemoney.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.usemoney.R
import com.android.usemoney.ui.add.category.AddCategoryFragment
import com.android.usemoney.ui.add.change.AddChangeFragment
import com.android.usemoney.ui.add.plan.AddPlanFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AddActivity"
@AndroidEntryPoint
class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val bundle = intent.extras
        when(bundle?.getString("add")){
         "category"->switchFragment(AddCategoryFragment())
         "plan"->switchFragment(AddPlanFragment())
         "change"->switchFragment(AddChangeFragment())
        }
        when(bundle?.getString("edit")){
            "category"->switchFragment(AddCategoryFragment.newInstance(bundle.getString("editCategory")!!))
            "plan"->switchFragment(AddPlanFragment())
            "change"->switchFragment(AddChangeFragment.newInstance(bundle.getString("editChange")!!))
        }

    }
    private fun switchFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.addFragmentContainerView,fragment)
            .commit()
    }
    companion object {
        fun closeActivity(activity: AddActivity) {
        activity.finish()
        }
    }
}