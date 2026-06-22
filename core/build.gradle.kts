plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.junit5.android)
}

android {
    namespace = "kinyua.vivian.core"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
        aidl = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    lint {
        targetSdk = 36
    }
    testOptions {
        targetSdk = 36
    }
}

dependencies {
//    implementation(project(":domain"))
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    api(libs.bundles.lifecycle)
    api(libs.bundles.room)
    implementation(libs.bundles.camerax)
    implementation(libs.bundles.testing.unit)
    implementation(libs.bundles.testing.android)
    api(libs.bundles.coroutines)
    api(libs.kotlinx.coroutines.core)

    api(libs.hilt.android)
    api(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}