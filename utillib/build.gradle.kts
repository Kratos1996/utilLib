import org.apache.commons.logging.LogFactory

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization.android)
    id("maven-publish")
}

android {
    namespace = "com.ishant.utillib"
    compileSdk = 35

    defaultConfig {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        /* freeCompilerArgs = listOf("-Xjvm-default=compatibility")*/
    }
    lint {
        /*disable = "NullSafeMutableLiveData"*/
        disable += "NullSafeMutableLiveData"
    }
    androidResources {
        additionalParameters.add("--warn-manifest-validation")
    }

    buildFeatures { // Enables Jetpack Compose for this module
        compose = true
    }
}

dependencies {

    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.material)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.composeUiTooling)
    api(libs.bundles.composelibs )
    api(libs.bundles.image.libs)
    api(libs.bundles.room.libs)

    //multi-thread
    api(libs.bundles.asyncronous.libs)
}

tasks.register<Jar>("sourceJar") {
    from(android.sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
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