pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CyberFusion-AI"
include(":app")
include(":core:common")
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:security")
include(":domain:usecase")
include(":feature:home")
include(":feature:soc")
include(":feature:intelligence")
include(":feature:loganalysis")
include(":feature:incident")
include(":feature:grc")
include(":feature:reports")
include(":feature:settings")
