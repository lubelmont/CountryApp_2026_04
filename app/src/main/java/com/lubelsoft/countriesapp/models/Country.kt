package com.lubelsoft.countriesapp.models

import androidx.datastore.core.Serializer
import java.io.Serializable


data class Country(
    val name: CountryName,
    val flags: ClountryFlags
): Serializable

data class CountryName(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeName>? = null
): Serializable

data class NativeName(
    val official: String,
    val common: String
): Serializable

data class ClountryFlags(
    val png: String,
    val svg: String
): Serializable

data class CountryDetail(
    val flag: String,
    val name: CountryName,
    val capital: List<String>?,
    val region: String,
    val maps: CountryMaps
): Serializable

data class CountryMaps(
    val googleMaps: String,
    val openStreetMaps: String
): Serializable
