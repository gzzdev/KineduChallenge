package com.cuzztomgdev.kineduchallenge.data.utils

import java.math.BigInteger
import java.security.MessageDigest


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')
}

fun getTimestamp(): String {
    return System.currentTimeMillis().toString()
}

fun generateHash(ts: String, privateKey: String, publicKey: String): String {
    return (ts + privateKey + publicKey).md5()
}