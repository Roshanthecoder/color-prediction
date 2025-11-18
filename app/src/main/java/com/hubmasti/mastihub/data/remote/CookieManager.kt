package com.hubmasti.mastihub.data.remote

import com.hubmasti.mastihub.data.local.UserDataStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager : CookieJar {

    val cookieStore = HashMap<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies.toMutableList()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    // ✅ Load cookies from DataStore into memory
    suspend fun loadFromDataStore(userDataStore: UserDataStore) {
        val cookies = userDataStore.getCookies()
        cookies.forEach { cookie ->
            cookieStore[cookie.domain] = cookieStore.getOrDefault(cookie.domain, mutableListOf()).apply {
                add(cookie)
            }
        }
    }

    // ✅ Get current cookies (optional)
    fun getCookies(): List<Cookie> = cookieStore.values.flatten()
}
