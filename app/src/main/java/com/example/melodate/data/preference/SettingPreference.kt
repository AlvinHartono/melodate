package com.example.melodate.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences(private val dataStore: DataStore<Preferences>) {

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    suspend fun setDarkMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isEnabled
        }
    }
}
