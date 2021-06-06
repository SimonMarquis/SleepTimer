import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("com.android.application")
    kotlin("android")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val versionBuild = 1

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "fr.smarquis.sleeptimer"
        minSdk = 26
        targetSdk = 30
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
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}
