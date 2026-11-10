pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // REMOVED: maven("https://jitpack.io") - WireGuard now on Maven Central
    }
}
rootProject.name = "ModelApp"
include(":app")
