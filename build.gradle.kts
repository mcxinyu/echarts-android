import java.util.*

ext {
    extra["core_version"] = "1.0.2"

    Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
        forEach { key, value ->
            extra[key as String] = value
        }
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}