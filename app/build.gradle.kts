import org.gradle.api.JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}

val versionMajor = 1
val versionMinor = 3
val versionPatch = 2
val versionBuild = 0

android {
    compileSdk = 35
    defaultConfig {
        applicationId = "fr.smarquis.sleeptimer"
        namespace = "fr.smarquis.sleeptimer"
        minSdk = 26
        targetSdk = 35
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
        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
        debug {
            initWith(release)
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    packaging {
        resources.excludes.add("kotlin-tooling-metadata.json")
        resources.excludes.add("**/*.kotlin_builtins")
    }
    compileOptions {
        sourceCompatibility = VERSION_11
        targetCompatibility = VERSION_11
    }
    kotlinOptions {
        jvmTarget = VERSION_11.toString()
    }
}
