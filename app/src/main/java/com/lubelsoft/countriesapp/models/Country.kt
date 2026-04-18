package com.lubelsoft.countriesapp.models

data class Country(
    val name: CountryName,
    val flags: ClountryFlags
)

data class CountryName(
    val common: String,
    val official: String
)



data class ClountryFlags(
    val png: String,
    val svg: String
)


