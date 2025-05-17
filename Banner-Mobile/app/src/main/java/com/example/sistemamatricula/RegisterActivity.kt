package com.example.sistemamatricula


import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var userNameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userNameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        registerBtn = findViewById(R.id.register_btn)

        registerBtn.setOnClickListener {
            val username = userNameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                saveCredentials(username, password)

                // Mensaje de confirmación
                Toast.makeText(this, "Registro exitoso, inicie sesión", Toast.LENGTH_SHORT).show()

                // Volver al LoginActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Finaliza RegisterActivity para evitar volver con "atrás"
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para guardar credenciales en SharedPreferences
    private fun saveCredentials(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
}