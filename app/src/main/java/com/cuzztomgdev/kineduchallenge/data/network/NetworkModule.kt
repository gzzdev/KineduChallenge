package com.cuzztomgdev.kineduchallenge.data.network

import android.content.Context
import com.cuzztomgdev.kineduchallenge.BuildConfig
import com.cuzztomgdev.kineduchallenge.data.RepositoryImp
import com.cuzztomgdev.kineduchallenge.data.core.interceptors.AuthInterceptor
import com.cuzztomgdev.kineduchallenge.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong() // 5mb to memory cache
        val cacheDir = File(context.cacheDir, "http_cache")
        return Cache(cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Increase write timeout
            .build()
    }

    @Provides
    fun provideMarvelApiService(retrofit: Retrofit): MarvelApiService {
        return retrofit.create(MarvelApiService::class.java)
    }

}
