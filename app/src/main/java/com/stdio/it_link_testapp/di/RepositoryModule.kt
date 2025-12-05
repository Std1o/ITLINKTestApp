package com.stdio.it_link_testapp.di

import com.stdio.it_link_testapp.data.repository.ImageRepositoryImpl
import com.stdio.it_link_testapp.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindImageRepository(repository: ImageRepositoryImpl): ImageRepository
}