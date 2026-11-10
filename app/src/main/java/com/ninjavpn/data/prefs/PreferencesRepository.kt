package com.ninjavpn.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.rxjava3.RxDataStore
import androidx.datastore.rxjava3.RxDataStoreBuilder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val context: Context,
) {
    private val dataStore: RxDataStore<Preferences> = RxDataStoreBuilder(context, "ninja_vpn_prefs").build()

    // Keys
    private val KEY_AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    private val KEY_KILL_SWITCH = booleanPreferencesKey("kill_switch")
    private val KEY_PROTOCOL = stringPreferencesKey("protocol")
    private val KEY_DNS_OVERRIDE = stringPreferencesKey("dns_override")
    private val KEY_LAST_SERVER_ID = stringPreferencesKey("last_server_id")
    private val KEY_THEME = intPreferencesKey("theme")
    private val KEY_LANGUAGE = stringPreferencesKey("language")
    private val KEY_FIRST_LAUNCH = booleanPreferencesKey("first_launch")

    suspend fun getAutoConnect(): Boolean = dataStore.data
        .map { it[KEY_AUTO_CONNECT] ?: true }
        .first()

    suspend fun setAutoConnect(enabled: Boolean) = dataStore.updateData { it[KEY_AUTO_CONNECT] = enabled }

    suspend fun getKillSwitch(): Boolean = dataStore.data
        .map { it[KEY_KILL_SWITCH] ?: false }
        .first()

    suspend fun setKillSwitch(enabled: Boolean) = dataStore.updateData { it[KEY_KILL_SWITCH] = enabled }

    suspend fun getProtocol(): String = dataStore.data
        .map { it[KEY_PROTOCOL] ?: "WIREGUARD" }
        .first()

    suspend fun setProtocol(protocol: String) = dataStore.updateData { it[KEY_PROTOCOL] = protocol }

    suspend fun getDnsOverride(): String? = dataStore.data
        .map { it[KEY_DNS_OVERRIDE] }
        .first()

    suspend fun setDnsOverride(dns: String?) = dataStore.updateData { it[KEY_DNS_OVERRIDE] = dns }

    suspend fun getLastServerId(): String? = dataStore.data
        .map { it[KEY_LAST_SERVER_ID] }
        .first()

    suspend fun setLastServerId(id: String?) = dataStore.updateData { it[KEY_LAST_SERVER_ID] = id }

    suspend fun getTheme(): Int = dataStore.data
        .map { it[KEY_THEME] ?: 0 }
        .first()

    suspend fun setTheme(theme: Int) = dataStore.updateData { it[KEY_THEME] = theme }

    suspend fun isFirstLaunch(): Boolean = dataStore.data
        .map { it[KEY_FIRST_LAUNCH] ?: true }
        .first()

    suspend fun setFirstLaunchComplete() = dataStore.updateData { it[KEY_FIRST_LAUNCH] = false }
}
