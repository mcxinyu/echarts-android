import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-module-name=com.mcxinyu.echartsandroid"
    }
    android.buildFeatures.dataBinding = true
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.mcxinyu"
                artifactId = "echarts-android"

                val time = SimpleDateFormat("yyyyMMddHHmm").format(Date())
                version = if ("true" == rootProject.extra["maven_local"]) "1.0.0-$time-SNAPSHOT"
                else Versions.CORE_LIB_VERSION

                from(components["release"])

                println("$artifactId version $version")
            }
        }
    }
}

dependencies {
    compileOnly("androidx.core:core-ktx:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}")

    compileOnly("com.google.code.gson:gson:${Versions.GSON_VERSION}")
    compileOnly("com.github.mcxinyu.javascript-interface:javascript-interface:${Versions.javascript_interface_version}")
}