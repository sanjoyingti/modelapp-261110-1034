package com.ninjavpn.di

import com.ninjavpn.core.vpn.VpnManager
import com.ninjavpn.core.vpn.WireGuardVpnService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object VpnModule {
    @Provides
    @Singleton
    fun provideVpnManager(manager: VpnManager): VpnManager = manager
}