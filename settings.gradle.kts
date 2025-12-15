pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
include(":presentation")
include(":app")
include(":shared")
project(":shared").projectDir = file("shared/shared")
rootProject.name = "ma-starter-android"