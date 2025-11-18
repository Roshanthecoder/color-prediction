package com.hubmasti.mastihub.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.Cookie

val Context.userDataStore by preferencesDataStore("user_prefs")

class UserDataStore(private val context: Context) {


    val COOKIES_KEY: String = "cookies_key"
    companion object {
        val KEY_MOBILE = stringPreferencesKey("mobile")
        val KEY_TOKEN = stringPreferencesKey("token")
        val KEY_USER = stringPreferencesKey("user")
        val KEY_COOKIES = stringPreferencesKey("cookies")
    }

    // SAVE DATA
    suspend fun saveLoginData(
        mobile: String,
        token: String,
        user: String,
        cookies: String
    ) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_MOBILE] = mobile
            prefs[KEY_TOKEN] = token
            prefs[KEY_USER] = user
            prefs[KEY_COOKIES] = cookies
        }
    }

    // READ DATA
    val mobile: Flow<String> = context.userDataStore.data.map { it[KEY_MOBILE] ?: "" }
    val token: Flow<String> = context.userDataStore.data.map { it[KEY_TOKEN] ?: "" }
    val user: Flow<String> = context.userDataStore.data.map { it[KEY_USER] ?: "" }
    val cookies: Flow<String> = context.userDataStore.data.map { it[KEY_COOKIES] ?: "" }

    // CLEAR ALL
    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
    suspend fun saveCookies(cookies: List<Cookie>) {
        val serialized = cookies.joinToString(";") { "${it.name}=${it.value}" }
        context.userDataStore.edit { it[KEY_COOKIES] = serialized }
    }

    suspend fun getCookies(): List<Cookie> {
        val serialized = context.userDataStore.data.first()[KEY_COOKIES] ?: ""
        return serialized.split(";").mapNotNull {
            val parts = it.split("=")
            if (parts.size == 2) Cookie.Builder().name(parts[0]).value(parts[1]).domain("mantrishop.in").build() else null
        }
    }

}
