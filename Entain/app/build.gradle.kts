plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = SdkConst.BASE_NAMESPACE + "entain"
    compileSdk = SdkConst.TARGET_COMPILE_SDK

    defaultConfig {
        applicationId = SdkConst.BASE_NAMESPACE + "entain"
        minSdk = SdkConst.MIN_SDK
        targetSdk = SdkConst.TARGET_COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = SdkConst.javaVersion
        targetCompatibility = SdkConst.javaVersion
    }
    kotlinOptions {
        jvmTarget = SdkConst.JVM_TARGET
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/AL2.0",
                "META-INF/AL2.0'",
                "META-INF/INDEX.LIST",
                "META-INF/io.netty.versions.properties",
                "META-INF/core_release.kotlin_module",
                "**/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/LGPL2.1",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(project(":component:backend"))
    implementation(project(":presentation:shared"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    debugImplementation(libs.leakcanary.android)

    implementation(libs.viewmodel.ktx)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}