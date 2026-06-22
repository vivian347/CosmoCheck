plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.hilt.core)

    testImplementation(libs.junit)
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}