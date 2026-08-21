package com.cyberfusion.ai.core.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cyberfusion_settings")

@Singleton
class SecurePreferences @Inject constructor(
    private val context: Context
) {
    private val apiKeyKey = stringPreferencesKey("ai_api_key")
    private val providerKey = stringPreferencesKey("ai_provider")

    val aiApiKey: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[apiKeyKey]
    }

    val aiProvider: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[providerKey]
    }

    suspend fun setAiApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[apiKeyKey] = key
        }
    }

    suspend fun setAiProvider(provider: String) {
        context.dataStore.edit { preferences ->
            preferences[providerKey] = provider
        }
    }
}
