package com.example.cookpad.view

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.cookpad.R
import com.example.cookpad.view.fragment.food.FoodFragment
import com.example.cookpad.view.fragment.group.GroupFragment
import android.content.Intent
import com.example.cookpad.view.fragment.discover.DiscoverFragment


class HomeActivity : AppCompatActivity() {

    val fragment1: androidx.fragment.app.Fragment = DiscoverFragment()
    val fragment2: androidx.fragment.app.Fragment = FoodFragment()
    val fragment3: androidx.fragment.app.Fragment = GroupFragment()
    val fm = supportFragmentManager

    var active = fragment1

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return@OnNavigationItemSelectedListener true;
            }
            R.id.navigation_home -> {
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return@OnNavigationItemSelectedListener true;
            }

            R.id.navigation_notifications -> {
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return@OnNavigationItemSelectedListener true;
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit()


    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}
