package com.lubelsoft.countriesapp.screens

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.adapter.CountriesAdapter
import com.lubelsoft.countriesapp.services.RetrofitClient
import com.lubelsoft.countriesapp.utils.UserPreferences
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var rvCountries: RecyclerView
    private lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Inicializar
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        rvCountries = findViewById(R.id.rvCountries)
        userPreferences = UserPreferences(this)


        //Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)


        //Datos del usuario
        val userName = intent.getStringExtra("USER_NAME")?: "N/A"
        val userEmail = intent.getStringExtra("USER_MAIL")?: "N/A"


        //Configurar header del navigation
        val headerView = navigationView.getHeaderView(0)

        headerView.findViewById<TextView>(R.id.tvUserName).text = userName
        headerView.findViewById<TextView>(R.id.tvUserEmail).text = userEmail



        //Uso de Menu
        navigationView.setNavigationItemSelectedListener { menuItem ->

            when(menuItem.itemId){
                R.id.nav_home -> {
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                   logout()
                    true
                }
                else-> false
            }


        }


        val rvCountries = findViewById<RecyclerView>(R.id.rvCountries)

        lifecycleScope.launch {
            try {

                val countries = RetrofitClient.api.getCountries()
                val adapter = CountriesAdapter(countries)

                rvCountries.adapter = adapter

            }catch (e: Exception){
                Toast.makeText(this@HomeActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()

            }
        }




    }
    private fun logout() {
        lifecycleScope.launch {
            userPreferences.clearUserSession()

            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        } else {
            drawerLayout.openDrawer(navigationView)
        }
        return true
    }



}