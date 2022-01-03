import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "nl.robrobproductions"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("com.1stleg:jnativehook:2.0.2")
    implementation("com.google.code.gson:gson:2.8.9")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            windows {
                iconFile.set(project.file("src/main/resources/assets/icons/program/phasmo-helper.ico"))

                modules("java.instrument", "java.sql", "jdk.unsupported")

                targetFormats(TargetFormat.Msi)
                packageName = "phasmo-helper"
                packageVersion = "1.0.0"

                appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            }
        }
    }
}