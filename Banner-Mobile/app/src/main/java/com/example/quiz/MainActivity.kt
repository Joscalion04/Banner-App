package com.example.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.quiz.ui.login.LoginActivity
import com.example.quiz.ui.menu.MenuActivity

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

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    QuizTheme {
//        Greeting("Android")
//    }
//}
