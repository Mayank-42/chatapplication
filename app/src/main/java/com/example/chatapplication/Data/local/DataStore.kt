package com.example.chatapplication.Data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.authDataStore by preferencesDataStore(
    name = "auth_preferences"
)