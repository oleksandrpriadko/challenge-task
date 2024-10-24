plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm) apply (true)
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":component:shared"))

    implementation(libs.bundles.ktor)

    api(libs.kotlin.json.jvm)

    api(libs.kotlinx.datetime)
}

java {
    sourceCompatibility = SdkConst.javaVersion
    targetCompatibility = SdkConst.javaVersion
}

kapt {
    correctErrorTypes = true
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}