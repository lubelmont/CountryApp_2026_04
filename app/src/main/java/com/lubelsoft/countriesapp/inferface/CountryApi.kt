package com.lubelsoft.countriesapp.inferface

import com.lubelsoft.countriesapp.models.Country
import retrofit2.http.GET

interface CountryApi {
    @GET("v3.1/region/america?fields=name,flags")
    suspend fun getCountries(): List<Country>


}