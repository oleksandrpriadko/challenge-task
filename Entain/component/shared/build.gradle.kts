plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm) apply (true)
}

dependencies {
    api(libs.coroutines.core)
    api(libs.kermit)
    // testApi does not propagate kotest and mock as transitive dependency
    api(libs.bundles.kotestmockk)
}

java {
    sourceCompatibility = SdkConst.javaVersion
    targetCompatibility = SdkConst.javaVersion
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}