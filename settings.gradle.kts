pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.library" -> {
                    useModule("com.android.tools.build:gradle:${requested.version}")
                }
                "com.android.application" -> {
                    useModule("com.android.tools.build:gradle:${requested.version}")
                }
                "androidx.navigation.safeargs.kotlin" -> {
                    useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
                }
                "dagger.hilt.android" ->{
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
                }
            }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        jcenter()
    }
}

include(":app")

rootProject.name = "IMC Challenge"
rootProject.buildFileName = "build.gradle.kts"