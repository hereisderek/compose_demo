
plugins {
    id ("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "top.derek.timely"
    compileSdk = 32

    defaultConfig {
        applicationId = "nz.co.test"

        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            signingConfigs.getByName("debug")
            ext["enableCrashlytics"] = false
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha06"
        // kotlinCompilerExtensionVersion = "1.1.0-rc02"
    }
    packagingOptions {
        resources {
            excludes += "**/*.kotlin_module"
            excludes += "**/*.kotlin_module"
            excludes += "**/*.version"
            excludes += "**/kotlin/**"
            excludes += "**/*.txt"
            excludes += "**/*.xml"
            excludes += "**/*.properties"
            excludes += "**/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.recyclerView)

    /* appCompat */
    implementation(AndroidX.appCompat)
    implementation(AndroidX.appCompat.resources)

    /* Coroutines */
    implementation(KotlinX.coroutines.android)

    /* Hilt */
    implementation (Google.dagger.hilt.android)
    kapt (Google.dagger.hilt.android.compiler)
    kapt (Google.dagger.hilt.compiler)

    implementation(Google.dagger.hilt.android)
    // implementation ("androidx.hilt:hilt-lifecycle-viewmodel:_")
    kapt (AndroidX.hilt.compiler)
    // implementation (AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)


    /* lifecycle */
    implementation(AndroidX.lifecycle.runtimeKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.lifecycle.viewModelCompose)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelSavedState)
    implementation(AndroidX.lifecycle.commonJava8)
    implementation(AndroidX.lifecycle.process)
    implementation(AndroidX.lifecycle.reactiveStreamsKtx)

    testImplementation(AndroidX.archCore.testing)

    /* Compose */
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.material.icons.core)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.compose.runtime.liveData)
    debugImplementation(AndroidX.compose.ui.testManifest)

    /* Coil */
    implementation(COIL.compose)

    /* Logging */
    implementation(Square.logcat)

    /*  Networking */
    implementation(Square.okio)
    implementation(Square.retrofit2)
    implementation(Square.okHttp3)
    implementation(Square.retrofit2.converter.gson)

    implementation(Ktor.client.core)
    implementation(Ktor.client.cio)

    /* Local testing */
    testImplementation(Testing.junit4)
    testImplementation("org.hamcrest:hamcrest-all:_")
    testImplementation(AndroidX.archCore.testing)
    testImplementation(KotlinX.coroutines.android)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(Testing.robolectric)
    testImplementation(AndroidX.navigation.testing)
    testImplementation(AndroidX.test.espresso.core)
    testImplementation(AndroidX.test.espresso.contrib)
    testImplementation(AndroidX.test.espresso.intents)
    testImplementation("com.google.truth:truth:_")

    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)
}