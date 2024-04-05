pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mondly test"
include(":app")

include(":testing")
include(":shared-types")
include(":viewmodel-util")
include(":main:ui")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":photos:ui")
include(":photos:domain")
include(":photos:data")
include(":design-system")
