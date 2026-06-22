plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))



    testImplementation(libs.junit)
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

//    implementation(libs.hilt.core)
}