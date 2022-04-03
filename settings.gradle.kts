pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("de.fayard.refreshVersions").version("0.40.1")
    }
}


plugins {
    id("com.gradle.enterprise").version("3.8")
    id("de.fayard.refreshVersions").version("0.40.1")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Timely"
include(":app")
