package com.lubelsoft.countriesapp.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lubelsoft.countriesapp.utils.UserPreferences
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar splash screen ANTES de setContentView
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = UserPreferences(this)

        // Simular carga (2 segundos)
        lifecycleScope.launch {
            try {
                // Verificar si el usuario está logeado
                userPreferences.isLoggedIn.collect { isLoggedIn ->
                    Thread.sleep(4000) // Mostrar splash por 2 segundos

                    val intent = if (isLoggedIn) {
                        // Ir a HomeActivity
                        Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    } else {
                        // Ir a MainActivity (login)
                        Intent(this@SplashScreenActivity, MainActivity::class.java)
                    }

                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}