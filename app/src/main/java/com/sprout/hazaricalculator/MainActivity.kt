package com.sprout.hazaricalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sprout.hazaricalculator.view.DashboardFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container,DashboardFragment.newInstance(),null)
            .addToBackStack(null)
            .commit()
    }
}