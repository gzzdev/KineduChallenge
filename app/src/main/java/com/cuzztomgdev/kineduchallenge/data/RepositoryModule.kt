package com.cuzztomgdev.kineduchallenge.data

import com.cuzztomgdev.kineduchallenge.data.local.CreatorDao
import com.cuzztomgdev.kineduchallenge.data.network.MarvelApiService
import com.cuzztomgdev.kineduchallenge.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideComicRepository(
        apiService: MarvelApiService,
        creatorDao: CreatorDao
    ): Repository = RepositoryImp(apiService, creatorDao)

}