package com.example.recyclops.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recyclops.R
import com.example.recyclops.databinding.ActivityMainBinding
import com.example.recyclops.ui.camera.CameraActivity
import com.example.recyclops.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.logout, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
//        R.id.logout -> {
//            Firebase.auth.signOut()
//            AuthUI.getInstance().signOut(this).addOnCompleteListener {
//                LoginActivity().updateUI(null)
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
//            true
//        }
//
//        else -> super.onOptionsItemSelected(item)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_map,
                R.id.navigation_poin,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.background = null
        navView.menu.getItem(2).isEnabled = false

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        binding.fabCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}