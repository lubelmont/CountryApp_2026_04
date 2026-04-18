package com.lubelsoft.countriesapp.screens

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.services.RetrofitClient
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val username = intent.getStringExtra("USER_NAME")


        lifecycleScope.launch {
            try {

                val countries = RetrofitClient.api.getCountries()

                val nombres = countries.joinToString("\n") { it.name.common }
                tvResult.text = nombres



            }catch (e: Exception){
                tvResult.text = e.message

            }
        }


    }
}