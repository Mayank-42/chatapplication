package com.example.chatapplication.Data.local


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(
    name = "auth_preferences"
)

class TokenManager(private val context: Context) {
    private val accessTokenKey =
        stringPreferencesKey("access_token")

    private val refreshTokenKey =
        stringPreferencesKey("refresh_token")

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        context.dataStore.edit { preferences ->

            preferences[accessTokenKey] = accessToken
            preferences[refreshTokenKey] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[accessTokenKey]
    }

    suspend fun getRefreshToken(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[refreshTokenKey]
    }

    suspend fun getUserId(): String? {

        val token = getAccessToken() ?: return null

        val payload = token.split(".")[1]

        // decode payload
        val decoded = android.util.Base64.decode(
            payload,
            android.util.Base64.URL_SAFE
        )

        val json = String(decoded)

        return org.json.JSONObject(json).getString("sub")
    }
    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
        }
    }
}