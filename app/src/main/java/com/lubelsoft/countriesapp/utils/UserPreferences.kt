package com.lubelsoft.countriesapp.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("user_preferences")


class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_MAIL = stringPreferencesKey("user_mail")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    val isLoggedIn : Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    fun getUserName(): Flow<String> = dataStore.data.map { it[USER_NAME] ?: "N/A" }
    fun getUserMail(): Flow<String> = dataStore.data.map { it[USER_MAIL] ?: "N/A" }
    fun getUserRole(): Flow<String> = dataStore.data.map { it[USER_ROLE] ?: "N/A" }


    suspend fun saveUserSession(userName: String, userMail: String, userRole: String) {
        dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[USER_NAME] = userName
            it[USER_MAIL] = userMail
            it[USER_ROLE] = userRole
        }
    }

    suspend fun clearUserSession() {
        dataStore.edit {
            it.clear()
        }
    }

}