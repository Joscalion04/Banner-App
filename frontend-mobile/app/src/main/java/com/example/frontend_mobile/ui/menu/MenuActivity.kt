package com.example.frontend_mobile.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.example.frontend_mobile.R
import com.example.frontend_mobile.ui.cursos.CursoFragment
import com.example.frontend_mobile.ui.login.LoginActivity
import com.example.frontend_mobile.ui.profesores.ProfesorFragment

class MenuActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("is_logged_in", false)
                        apply()
                    }

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
                }
                R.id.cursos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CursoFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.profesores -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfesorFragment())
                        .addToBackStack(null)
                        .commit()
                }
//                else -> {
//                    false
//                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
