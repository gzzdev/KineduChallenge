package com.cuzztomgdev.kineduchallenge.data.core.interceptors

import com.cuzztomgdev.kineduchallenge.BuildConfig
import com.cuzztomgdev.kineduchallenge.data.core.utils.generateHash
import com.cuzztomgdev.kineduchallenge.data.core.utils.getTimestamp
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = getTimestamp()
        val hash = generateHash(ts, BuildConfig.PRIVATE_API_KEY, BuildConfig.PUBLIC_API_KEY)

        val originalRequest = chain.request()
        val urlWithAuth = originalRequest.url
            .newBuilder()
            .addQueryParameter("ts", ts)
            .addQueryParameter("apikey", BuildConfig.PUBLIC_API_KEY)
            .addQueryParameter("hash", hash)
            .build()

        val requestWithAuth = originalRequest.newBuilder()
            .url(urlWithAuth)
            .build()

        return chain.proceed(requestWithAuth)
    }


}