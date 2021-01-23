import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version "4.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.4.21" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.3.2" apply false
    id("com.diffplug.gradle.spotless") version "4.5.1"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

subprojects {
    apply {
        plugin("com.diffplug.gradle.spotless")
    }

    val ktlintVer = "0.39.0"

    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVer)
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(ktlintVer)
        }
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            mustRunAfter("spotlessCheck")
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=" +
                "kotlin.Experimental," +
                "kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}