package com.example.sistemamatricula


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MenuActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Configurar el BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Cargar el fragmento Home
                    true
                }
                R.id.nav_profile -> {
                    // Cargar el fragmento Perfil
                    true
                }
                R.id.nav_settings -> {
                    // Cargar el fragmento Configuración
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        // Configurar el menú lateral (Drawer)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> true
                R.id.nav_settings -> true
                R.id.nav_logout -> { logout(); true }
                else -> false
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}