package com.lubelsoft.countriesapp.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.models.Country
import com.lubelsoft.countriesapp.models.CountryDetail

class CountryDeatailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_country_deatail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se intenta obtener CountryDetail o Country desde el Intent
        val countryDetail = intent.getSerializableExtra("country_detail") as? CountryDetail
        val country = intent.getSerializableExtra("country") as? Country

        if (countryDetail != null) {
            displayCountryDetail(countryDetail)
        } else if (country != null) {
            displayBasicCountry(country)
        }
    }

    private fun displayCountryDetail(detail: CountryDetail) {
        val ivDetailFlag = findViewById<ImageView>(R.id.ivDetailFlag)
        val tvDetailName = findViewById<TextView>(R.id.tvDetailName)
        val tvDetailOfficialName = findViewById<TextView>(R.id.tvDetailOfficialName)
        val tvDetailCapital = findViewById<TextView>(R.id.tvDetailCapital)
        val tvDetailRegion = findViewById<TextView>(R.id.tvDetailRegion)
        val tvDetailNativeName = findViewById<TextView>(R.id.tvDetailNativeName)
        val btnOpenMaps = findViewById<Button>(R.id.btnOpenMaps)

        tvDetailName.text = detail.name.common
        tvDetailOfficialName.text = detail.name.official
        tvDetailCapital.text = "Capital: ${detail.capital?.joinToString(", ") ?: "N/A"}"
        tvDetailRegion.text = "Región: ${detail.region}"
        
        val nativeName = detail.name.nativeName?.values?.firstOrNull()
        tvDetailNativeName.text = "Nombre Nativo: ${nativeName?.common ?: "N/A"}"

        // Si detail.flag es un emoji, podrías mostrarlo en un TextView.
        // Aquí mostramos cómo abrir el mapa:
        btnOpenMaps.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.maps.googleMaps))
            startActivity(mapIntent)
        }
    }

    private fun displayBasicCountry(country: Country) {
        findViewById<TextView>(R.id.tvDetailName).text = country.name.common
        findViewById<TextView>(R.id.tvDetailOfficialName).text = country.name.official
        
        Glide.with(this)
            .load(country.flags.png)
            .into(findViewById(R.id.ivDetailFlag))
    }
}
