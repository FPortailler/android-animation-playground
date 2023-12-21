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
        maven { url = java.net.URI("https://jitpack.io") }
    }

    versionCatalogs {
        create("shared") {
            from(files("./shared.versions.toml"))
        }
        create("appLibs") {
            from(files("./app/app.versions.toml"))
        }
    }
}
rootProject.name = "testAnimation"
include(":app")
