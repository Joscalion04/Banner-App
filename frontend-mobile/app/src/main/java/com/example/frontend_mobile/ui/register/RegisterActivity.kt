package com.example.frontend_mobile.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.frontend_mobile.R
import com.example.frontend_mobile.databinding.ActivityRegisterBinding
import com.example.frontend_mobile.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registerUsername = binding.RegisterUsername
        val registerPassword = binding.RegisterPassword
        val confirmRegister = binding.confirmRegister
        binding.loading

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())[RegisterViewModel::class.java]

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            if (confirmRegister != null) {
                confirmRegister.isEnabled = loginState.isDataValid
            }

            if (loginState.usernameError != null) {
                if (registerUsername != null) {
                    registerUsername.error = getString(loginState.usernameError)
                }
            }
            if (loginState.passwordError != null) {
                if (registerPassword != null) {
                    registerPassword.error = getString(loginState.passwordError)
                }
            }
        })

        confirmRegister.setOnClickListener {
            val username = registerUsername.text.toString()
            val password = registerPassword.text.toString()

            registerViewModel.register(username, password) { result ->
                if (!result) {
                    showRegisterFailed()
                } else {
                    updateUiWithUser()
                    setResult(RESULT_OK)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        registerUsername.afterTextChanged {
            if (registerPassword != null) {
                registerViewModel.registerDataChanged(
                    registerUsername.text.toString(),
                    registerPassword.text.toString()
                )
            }
        }

        registerPassword.apply {
            afterTextChanged {
                if (registerUsername != null) {
                    registerViewModel.registerDataChanged(
                        registerUsername.text.toString(),
                        registerPassword.text.toString()
                    )
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        val username = registerUsername.text.toString()
                        val password = registerPassword.text.toString()

                        registerViewModel.register(username, password) { result ->
                            if (!result) {
                                showRegisterFailed()
                            } else {
                                updateUiWithUser()
                                setResult(RESULT_OK)
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }

        val backButton: Button = findViewById(R.id.back)

        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUiWithUser() {
        Toast.makeText(
            applicationContext,
            "Usuario registrado correctamente",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showRegisterFailed() {
        Toast.makeText(
            applicationContext,
            "El usuario ya se encuentra registrado",
            Toast.LENGTH_LONG
        ).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}