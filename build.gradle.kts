// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenCentral()
        flatDir {
            dirs = setOf(file("libs"))
        }
        mavenLocal()
    }
    dependencies {
        /*  classpath("com.google.dagger:hilt-android-gradle-plugin:${hiltVersion}")*/
        classpath(libs.kotlin.gradle.plugin)
    }
}
