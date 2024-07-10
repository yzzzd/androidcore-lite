plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    /* firebase */
    // alias(libs.plugins.google.service)
    // alias(libs.plugins.firebase.crashlytics)
    // alias(libs.plugins.firebase.perf)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.nuvyz.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nuvyz.app"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            manifestPlaceholders["crashlyticsEnabled"] = false
            manifestPlaceholders["performanceEnabled"] = false
            manifestPlaceholders["useClearText"] = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsEnabled"] = true
            manifestPlaceholders["performanceEnabled"] = true
            manifestPlaceholders["useClearText"] = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    /* dependency injection */
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    /* database */
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}