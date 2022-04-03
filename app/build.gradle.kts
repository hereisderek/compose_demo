
plugins {
    id ("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "top.derek.timely"
    compileSdk = 32

    defaultConfig {
        applicationId = "top.derek.timely"

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
    implementation(AndroidX.lifecycle.runtimeKtx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0-alpha05")

    /* coroutines */
    implementation(KotlinX.coroutines.android)

    /* Hilt */
    implementation (AndroidX.hilt.compiler)
    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:_")
    kapt (AndroidX.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)


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

    implementation(Ktor.client.core)
    implementation(Ktor.client.cio)

    testImplementation(Testing.junit4)
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)



}