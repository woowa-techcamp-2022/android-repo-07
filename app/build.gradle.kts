import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "org.woowatechcamp.githubapplication"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLIENT_ID", properties.getProperty("github_client_id"))
        buildConfigField("String", "CLIENT_SECRETS", properties.getProperty("github_client_secrets"))
        buildConfigField("String", "GITHUB_BASE", properties.getProperty("github_base_url"))
        buildConfigField("String", "GITHUB_API", properties.getProperty("github_api_url"))
        buildConfigField("String", "GITHUB_AUTH", properties.getProperty("github_auth_url"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "11"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    namespace = "org.woowatechcamp.githubapplication"
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.dagger)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.gson.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.bom)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.junit)
    implementation(libs.material.design)
    kapt(libs.bundles.compiler)
}
