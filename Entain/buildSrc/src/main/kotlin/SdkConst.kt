import org.gradle.api.JavaVersion

object SdkConst {
    const val TARGET_COMPILE_SDK = 34
    const val MIN_SDK = 28
    const val JVM_TARGET = "21"
    val javaVersion = JavaVersion.VERSION_21
    const val BASE_NAMESPACE = "com.oleksandrpriadko"
}