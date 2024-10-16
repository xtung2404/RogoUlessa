import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.google.code.gson:gson:2.9.0")
                implementation("org.json:json:20240205")
                implementation("org.jetbrains:annotations:23.1.0")
                implementation ("org.eclipse.paho:org.eclipse.paho.mqttv5.client:1.2.5")
                implementation ("io.github.rburgst:okhttp-digest:2.6") //CAMERA IP
                //SQLITE
                implementation("org.xerial:sqlite-jdbc:3.46.1.0")
                //HTTP
                implementation("com.squareup.okhttp3:okhttp:4.9.3")

                implementation("org.slf4j:slf4j-api:2.0.12")
                implementation("org.slf4j:slf4j-simple:2.0.12")
                implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "ThingEdges"
            modules("jdk.crypto.ec")
            modules("java.sql")

            packageVersion = "1.0.0"
        }
    }
}
