import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("com.android.application")
    id("kotlin-android")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val versionBuild = 0

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")
    defaultConfig {
        applicationId = "fr.smarquis.sleeptimer"
        minSdkVersion(26)
        targetSdkVersion(30)
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
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
        getByName("debug") {
            initWith(getByName("release"))
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

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
}