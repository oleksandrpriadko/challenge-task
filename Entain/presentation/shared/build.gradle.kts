plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = (SdkConst.BASE_NAMESPACE + ".shared")
    compileSdk = SdkConst.TARGET_COMPILE_SDK

    defaultConfig {
        minSdk = SdkConst.MIN_SDK
        lint.targetSdk = SdkConst.TARGET_COMPILE_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    api(project(":component:shared"))

    api(libs.core.ktx)

    api(libs.material)

    api(libs.lifecycle.runtime.ktx)
}