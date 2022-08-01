package com.android.usemoney.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
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
        val key = bundle?.getString("add")
        Log.d(TAG,"$key")
        if (key == "category") {
         switchFragment(AddCategoryFragment())
        }
        if (key == "plan"){
            switchFragment(AddPlanFragment())
        }
        if (key == "change"){
            switchFragment(AddChangeFragment())
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