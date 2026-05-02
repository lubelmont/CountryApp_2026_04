package com.lubelsoft.countriesapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.models.Country
import com.lubelsoft.countriesapp.screens.CountryDeatailActivity

class CountriesAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flag: ImageView = itemView.findViewById(R.id.ivCountryFlag)
        val name: TextView = itemView.findViewById(R.id.tvCountryName)
        val nameOfficial: TextView = itemView.findViewById(R.id.tvCountryNameOfficial)

        fun bind(country: Country) {
            name.text = country.name.common
            nameOfficial.text = country.name.official

            Glide.with(itemView.context)
                .load(country.flags.png)
                .into(flag)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CountryDeatailActivity::class.java)
                intent.putExtra("country", country)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int
    ) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size
}
