import com.android.build.gradle.internal.utils.createPublishingInfoForLibrary

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.nuvyz.core"
    compileSdk = 34

    group = "com.nuvyz.core"
    version = "2.0.0"

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
}

dependencies {

    api(libs.androidx.activity.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.androidx.core.splashscreen)
    api(libs.androidx.paging.runtime.ktx)
    api(libs.androidx.security.crypto)
    api(libs.androidx.swiperefreshlayout)
    api(libs.material)

    /* design pattern */
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)

    /* firebase */
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics) {
        exclude(group = "play-services-ads-identifier")
        exclude(group = "play-services-measurement")
        exclude(group = "play-services-measurement-sdk")
    }
    api(libs.firebase.crashlytics)
    api(libs.firebase.perf)
    api(libs.firebase.messaging)

    /* location */
    api(libs.play.services.maps)
    api(libs.play.services.location)

    /* networking */
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.gson)
    api(libs.coil)

    /* utils */
    api(libs.eventbus)
    api(libs.compressor)
    api(libs.stfalconimageviewer)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.yzzzd"
                artifactId = "androidcore-lite"
                version = "2.0.0"
            }
        }
    }
}