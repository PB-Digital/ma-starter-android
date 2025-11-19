plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    //DI
    implementation(libs.koin.core)

    testImplementation(libs.junit)
}