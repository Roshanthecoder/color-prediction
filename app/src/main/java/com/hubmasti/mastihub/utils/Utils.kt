package com.hubmasti.mastihub.utils

import java.security.MessageDigest

object Utils {

    fun md5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())

        return digest.joinToString("") {
            "%02x".format(it)
        }
    }

}