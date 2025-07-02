plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "backend"
include(":db")
include(":admin")
include(":shared")
include(":testUtil")
include(":teacher")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}
