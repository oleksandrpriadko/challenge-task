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

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    debugImplementation(libs.leakcanary.android)
    testImplementation(libs.android.test.core)

    implementation(libs.fragment.kts)
    implementation(libs.recyclerview)
    implementation(libs.viewmodel.ktx)
    implementation(libs.livedata.ktx)
    kapt(libs.lifecycle.compiler)

    implementation(libs.coil)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}