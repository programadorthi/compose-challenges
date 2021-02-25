package dev.programadorthi

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha08"

    object Kotlin {
        const val version = "1.4.30"

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val material = "com.google.android.material:material:1.3.0"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha03"
        }

        object Compose {
            const val version = "1.0.0-beta01"

            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val junitAndroidX = "androidx.test.ext:junit:1.1.2"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
    }
}