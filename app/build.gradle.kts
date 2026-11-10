plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android") version "2.51.1"
    id("kotlinx-serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.ninjavpn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ninjavpn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1413
        versionName = "1.4.13-free"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            val keystoreFile = file("signing.keystore")
            if (keystoreFile.exists()) {
                storeFile = keystoreFile
                storePassword = "android"
                keyAlias = "app"
                keyPassword = "android"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-P", "plugin:kotlinx-serialization:classSerializers=explicit"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.20"
    }

    packagingOptions {
        resources.excludes += "META-INF/*.kotlin_module"
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)

    // Hilt DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)

    // DataStore Preferences
    implementation(libs.datastore.preferences)

    // WireGuard Go Backend (from Maven Central)
    implementation(libs.wireguard.android)

    // Core library desugaring for Java 8+ APIs (required by WireGuard tunnel library)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // Timber Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Coil Image Loading (for country flags if needed)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // REMOVED: Play Billing, Google Ads, Firebase, Analytics, Crashlytics
}

// REMOVED: kapt { correctErrorTypes = true } - deprecated/removed in newer Kotlin versions
