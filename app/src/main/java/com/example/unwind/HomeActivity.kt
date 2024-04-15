package com.example.unwind

//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class HomeActivity : AppCompatActivity() {
//    private lateinit var welcome_back: TextView
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.home_page_main)
//        welcome_back = findViewById(R.id.welcome_bac)
//        val userName = intent.getStringExtra("userName")
//        if (userName != null) {
//            welcome_back.text = "Welcome back, $userName!"
//        } else {
//            welcome_back.text = "Welcome back!"
//        }
//        val quotesFragment = QuotesFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, quotesFragment)
//            .commit()
//
//        val settingsButton = findViewById<View>(R.id.menu_vector)
//        settingsButton.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    //private lateinit var welcome_back: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_nav)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        toolbar.visibility = View.GONE
//        setSupportActionBar(toolbar)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            // For older versions, use system UI flags
//            @Suppress("DEPRECATION")
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
//        actionBar?.hide()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_nav)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_breathe, R.id.navigation_listen
        ))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}
