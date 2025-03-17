package com.cuzztomgdev.kineduchallenge.data.network

import com.cuzztomgdev.kineduchallenge.BuildConfig
import com.cuzztomgdev.kineduchallenge.data.RepositoryImp
import com.cuzztomgdev.kineduchallenge.data.core.interceptors.AuthInterceptor
import com.cuzztomgdev.kineduchallenge.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
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

    @Provides
    fun provideRepository(apiService: MarvelApiService): Repository {
        return RepositoryImp(apiService)
    }
}
