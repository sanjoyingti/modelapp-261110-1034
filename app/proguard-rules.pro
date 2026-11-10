# WireGuard
-keep class com.wireguard.** { *; }
-keep class com.ninjavpn.core.vpn.** { *; }

# Domain models (serialization)
-keep class com.ninjavpn.domain.model.** { *; }
-keep class kotlinx.serialization.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.EntryPoint class * { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Coil
-keep class coil3.** { *; }

# OkHttp (WireGuard uses it internally)
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep Parcelable for Server model
-keep class com.ninjavpn.domain.model.Server { *; }