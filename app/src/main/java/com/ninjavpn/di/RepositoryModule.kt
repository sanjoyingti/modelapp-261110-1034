package com.ninjavpn.di

import com.ninjavpn.data.repository.ServerRepository
import com.ninjavpn.domain.repository.ServerRepository as DomainServerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindServerRepository(impl: ServerRepository): DomainServerRepo
}