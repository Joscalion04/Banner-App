package com.example.frontend_mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.frontend_mobile.data.WebSocketManager
import com.example.frontend_mobile.ui.login.LoginActivity
import com.example.frontend_mobile.ui.menu.MenuActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica si el usuario ya inició sesión (puedes usar SharedPreferences)
        val isLoggedIn = checkUserLogin()

        if (!isLoggedIn) {
            // Si no ha iniciado sesión, redirigir a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Evita que el usuario regrese con el botón atrás
            return
        }

        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Evita que el usuario regrese con el botón atrás
        return
    }

    private fun checkUserLogin(): Boolean {
        // Aquí se puede verificar si el usuario ya inició sesión
        // Puedes usar SharedPreferences, Firebase Auth, etc.
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPref.getBoolean("is_logged_in", false) // Retorna 'false' si no hay datos
    }
}