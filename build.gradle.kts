// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath(Google.dagger.hilt.android.gradlePlugin)
    }
}

plugins {
    // id("com.android.application") version "7.3.0-alpha07" // version '7.3.0-alpha07'
    // id("com.android.library")// version '7.3.0-alpha07'
    // id("org.jetbrains.kotlin.android")
    // kotlin("android")

    // id 'org.jetbrains.kotlin.android' version '1.6.20-M1' apply false
    // id 'de.fayard.refreshVersions'
}
