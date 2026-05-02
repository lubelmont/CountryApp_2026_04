package com.lubelsoft.countriesapp.inferface

import com.lubelsoft.countriesapp.models.Country
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {
    @GET("v3.1/region/{region}?fields=name,flags")
    suspend fun getCountries(@Path("region") region: String): List<Country>

    @GET("v3.1/nombre/{name}?fields=name,capital,flag,maps,region")
    suspend fun getCountryByName(@Path("name") name: String): List<Country>




}