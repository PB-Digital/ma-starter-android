import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    //DI
    implementation(libs.koin.core)

    testImplementation(libs.junit)
}