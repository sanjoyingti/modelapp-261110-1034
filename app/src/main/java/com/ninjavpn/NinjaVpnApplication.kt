package com.ninjavpn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NinjaVpnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // REMOVED: FirebaseApp.initializeApp(this)
        // REMOVED: MobileAds.initialize(this)
        // REMOVED: ConsentManager.init(this)
    }
}