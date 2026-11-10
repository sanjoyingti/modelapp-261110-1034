package com.ninjavpn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ninja_vpn_prefs")

@Singleton
class PreferencesRepository @Inject constructor(
    private val context: Context,
) {
    // Keys
    private val KEY_AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    private val KEY_KILL_SWITCH = booleanPreferencesKey("kill_switch")
    private val KEY_PROTOCOL = stringPreferencesKey("protocol")
    private val KEY_DNS_OVERRIDE = stringPreferencesKey("dns_override")
    private val KEY_LAST_SERVER_ID = stringPreferencesKey("last_server_id")
    private val KEY_THEME = intPreferencesKey("theme")
    private val KEY_LANGUAGE = stringPreferencesKey("language")
    private val KEY_FIRST_LAUNCH = booleanPreferencesKey("first_launch")

    suspend fun getAutoConnect(): Boolean = context.dataStore.data
        .map { it[KEY_AUTO_CONNECT] ?: true }
        .first()

    suspend fun setAutoConnect(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_CONNECT] = enabled }
    }

    suspend fun getKillSwitch(): Boolean = context.dataStore.data
        .map { it[KEY_KILL_SWITCH] ?: false }
        .first()

    suspend fun setKillSwitch(enabled: Boolean) {
        context.dataStore.edit { it[KEY_KILL_SWITCH] = enabled }
    }

    suspend fun getProtocol(): String = context.dataStore.data
        .map { it[KEY_PROTOCOL] ?: "WIREGUARD" }
        .first()

    suspend fun setProtocol(protocol: String) {
        context.dataStore.edit { it[KEY_PROTOCOL] = protocol }
    }

    suspend fun getDnsOverride(): String? = context.dataStore.data
        .map { it[KEY_DNS_OVERRIDE] }
        .first()

    suspend fun setDnsOverride(dns: String?) {
        context.dataStore.edit { 
            if (dns != null) it[KEY_DNS_OVERRIDE] = dns 
            else it.remove(KEY_DNS_OVERRIDE)
        }
    }

    suspend fun getLastServerId(): String? = context.dataStore.data
        .map { it[KEY_LAST_SERVER_ID] }
        .first()

    suspend fun setLastServerId(id: String?) {
        context.dataStore.edit { 
            if (id != null) it[KEY_LAST_SERVER_ID] = id 
            else it.remove(KEY_LAST_SERVER_ID)
        }
    }

    suspend fun getTheme(): Int = context.dataStore.data
        .map { it[KEY_THEME] ?: 0 }
        .first()

    suspend fun setTheme(theme: Int) {
        context.dataStore.edit { it[KEY_THEME] = theme }
    }

    suspend fun isFirstLaunch(): Boolean = context.dataStore.data
        .map { it[KEY_FIRST_LAUNCH] ?: true }
        .first()

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { it[KEY_FIRST_LAUNCH] = false }
    }
}
