import java.util.*

ext {
    Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
        forEach { key, value -> extra[key as String] = value }
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}