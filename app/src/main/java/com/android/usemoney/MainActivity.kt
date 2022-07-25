package com.android.usemoney



import android.R.attr.button
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


private const val TAG ="MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var layoutChange: ConstraintLayout
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"onCreate")
        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
        R.id.changeFragment,R.id.cardFragment,R.id.historyFragment,R.id.planFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    companion object {
        fun startActivity(context: Context?){
            val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }
    }

}