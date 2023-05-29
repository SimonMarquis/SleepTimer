import org.gradle.api.JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}

val versionMajor = 1
val versionMinor = 2
val versionPatch = 1
val versionBuild = 1

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "fr.smarquis.sleeptimer"
        namespace = "fr.smarquis.sleeptimer"
        minSdk = 26
        targetSdk = 33
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    signingConfigs {
        getByName("debug") {
            keyAlias = "sleeptimer"
            keyPassword = "sleeptimer"
            storePassword = "sleeptimer"
            storeFile = file("debug.keystore")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = VERSION_11
        targetCompatibility = VERSION_11
    }
    kotlinOptions {
        jvmTarget = VERSION_11.toString()
    }
}
