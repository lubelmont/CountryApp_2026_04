package com.lubelsoft.countriesapp.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.adapter.CountriesAdapter
import com.lubelsoft.countriesapp.models.Country
import com.lubelsoft.countriesapp.services.RetrofitClient
import com.lubelsoft.countriesapp.utils.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var rvCountries: RecyclerView
    private lateinit var userPreferences: UserPreferences
    private lateinit var  searchView: SearchView

    private var currentRegion = "america"

    private var allCountries: List<Country> = emptyList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)



        //Inicializar
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        rvCountries = findViewById(R.id.rvCountries)
        userPreferences = UserPreferences(this)
        searchView = findViewById(R.id.searchView)




        //Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)


        //Datos del usuario




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

        setupContinentButtons()
        setupSearchView()
        loadCountriesByRegion(currentRegion)



        lifecycleScope.launch {
            try {

                val userName = getUserName();
                val userEmail = getUserMail();



                //Configurar header del navigation
                val headerView = navigationView.getHeaderView(0)

                headerView.findViewById<TextView>(R.id.tvUserName).text = userName
                headerView.findViewById<TextView>(R.id.tvUserEmail).text = userEmail


            }catch (e: Exception){
                Toast.makeText(this@HomeActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()

            }
        }




    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No hacemos nada al enviar la búsqueda
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText.isNullOrEmpty()){
                    loadCountriesByRegion(currentRegion)
                } else {
                    filterCountries(newText)
                }

                return true
            }
        })
    }

    private fun filterCountries(query: String) {
        val filteredCountries = allCountries.filter {
            it.name.common.contains(query, ignoreCase = true) ||
                    it.name.official.contains(query, ignoreCase = true)
        }

        rvCountries.adapter = CountriesAdapter(filteredCountries)
    }


    private fun loadCountriesByRegion(region: String){
        lifecycleScope.launch {
            try {
                allCountries= RetrofitClient.api.getCountries(region)
                val adapter = CountriesAdapter(allCountries)

                rvCountries.adapter = adapter

            }catch (e: Exception){
                Toast.makeText(this@HomeActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun setupContinentButtons(){
        val continentMap = mapOf(
            R.id.btnAmerica to "america",
            R.id.btnEuropa to "europe",
            R.id.btnAsia to "asia",
            R.id.btnAfrica to "africa",
            R.id.btnOceania to "oceania"
        )

        findViewById<View>(R.id.btnAmerica).isSelected = true

        continentMap.forEach { (buttonId, region) ->
            findViewById<View>(buttonId).setOnClickListener {view->
                continentMap.keys.forEach { id->
                    findViewById<View>(id).isSelected=false
                }

                view.isSelected = true

                currentRegion = region
                searchView.setQuery("", false)
                loadCountriesByRegion(region)
            }
        }




    }


    private suspend fun getUserName(): String {
        return userPreferences.getUserName().firstOrNull() ?: "N/A"
    }
    private suspend fun getUserMail(): String {
        return userPreferences.getUserMail().firstOrNull() ?: "N/A"
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