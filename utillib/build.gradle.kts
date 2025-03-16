import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization.android)
    id("maven-publish")
}


android {
    namespace = "com.techhub.utillib"
    compileSdk = 35

    defaultConfig {
        configurations.all {
            resolutionStrategy {
                exclude(group = "com.intellij", module = "annotations")
            }
            exclude(module = "bcprov-jdk18on")
        }
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
       release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs = listOf("-Xjvm-default=all-compatibility")
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        }
    }


    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/**/*"
        }
    }
    lint { disable += "NullSafeMutableLiveData" }

    androidResources {
        additionalParameters.add("--warn-manifest-validation")
        additionalParameters.add("--warning-mode all")
    }

    buildFeatures { // Enables Jetpack Compose for this module
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.annotations)
    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.material)
    implementation(libs.lay.services.location)
    implementation(libs.androidx.window)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.composeUiTooling)
    api(libs.bundles.composelibs )
    api(libs.bundles.image.libs)
    api(libs.bundles.room.libs)
    api(libs.bundles.firebase.libs.common)
    api(libs.annotations)
    //multi-thread
    api(libs.bundles.asyncronous.libs)
}

tasks.register<Jar>("sourceJar") {
    from(android.sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
publishing {
    repositories {
        maven {
            name = "utilLibCompose"
            url = uri("file://${buildDir}/repo")
        }
    }

    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.Kratos1996"
            artifactId = "utilLib"//project name on git
            version = "1.0.0"
            artifact("$buildDir/outputs/aar/utilLibCompose-release.aar")
            artifact(tasks["sourceJar"])

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                // Iterate over the api dependencies, adding a <dependency> node for each
                configurations["api"].allDependencies.forEach {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                }
            }
        }
    }
}