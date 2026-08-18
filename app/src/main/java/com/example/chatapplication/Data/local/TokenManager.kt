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
    private val appcontext = context.applicationContext
    private val accessTokenKey =
        stringPreferencesKey("access_token")

    private val refreshTokenKey =
        stringPreferencesKey("refresh_token")

    private val userIdKey =
        stringPreferencesKey("user_id")

//    suspend fun saveTokens(
//        accessToken: String,
//        refreshToken: String
//    ) {
//        val payload = accessToken.split(".")[1]
//
//        val decoded = android.util.Base64.decode(
//            payload,
//            android.util.Base64.URL_SAFE
//        )
//
//        val json = String(decoded)
//
//        val userId =
//            org.json.JSONObject(json).getString("sub")
//
//        context.dataStore.edit { preferences ->
//            preferences[accessTokenKey] = accessToken
//            preferences[refreshTokenKey] = refreshToken
//            preferences[userIdKey] = userId
//        }
//    }
suspend fun saveTokens(
    accessToken: String,
    refreshToken: String
) {
    val payload = accessToken.split(".")[1]

    val decoded = android.util.Base64.decode(
        payload,
        android.util.Base64.URL_SAFE
    )

    val json = String(decoded)

    val userId =
        org.json.JSONObject(json).getString("sub")

    println("TOKEN MANAGER: SAVING USER ID = $userId")

    appcontext.dataStore.edit { preferences ->
        preferences[accessTokenKey] = accessToken
        preferences[refreshTokenKey] = refreshToken
        preferences[userIdKey] = userId
    }

    println("TOKEN MANAGER: USER ID SAVED = ${getUserId()}")
}

    suspend fun getAccessToken(): String? {
        val preferences = appcontext.dataStore.data.first()
        return preferences[accessTokenKey]
    }

    suspend fun getRefreshToken(): String? {
        val preferences = appcontext.dataStore.data.first()
        return preferences[refreshTokenKey]
    }
    suspend fun getUserId(): String? {
        val preferences = appcontext.dataStore.data.first()
        val id = preferences[userIdKey]

        println("TOKEN MANAGER: GET USER ID = $id")

        return id
    }

//    suspend fun getUserId(): String? {
//        val preferences = context.dataStore.data.first()
//        return preferences[userIdKey]
//    }
    suspend fun clearTokens() {
        appcontext.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(userIdKey)
        }
    }
}