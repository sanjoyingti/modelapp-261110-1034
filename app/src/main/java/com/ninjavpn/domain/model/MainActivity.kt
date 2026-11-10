package com.ninjavpn.domain.model

// VERSION CATALOG (gradle/libs.versions.toml)
[versions]
agp = "8.7.0"
kotlin = "2.0.20"
composeBom = "2024.10.00"
hilt = "2.51.1"
room = "2.6.1"
work = "2.9.1"
datastore = "1.1.4"
playBilling = "7.0.0"
ads = "23.1.0"
firebaseBom = "33.4.0"
wireguard = "1.0.20241017"  // wireguard-android

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version = "1.15.0" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version = "2.8.4" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version = "2.8.4" }
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-runtime = { group = "androidx.compose.runtime", name = "runtime" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }
datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
play-billing = { group = "com.android.billingclient", name = "billing", version.ref = "playBilling" }
play-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "ads" }
firebase-analytics = { group = "com.google.firebase", name = "firebase-analytics-ktx" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
wireguard-android = { group = "com.wireguard.android", name = "wireguard-android", version.ref = "wireguard" }