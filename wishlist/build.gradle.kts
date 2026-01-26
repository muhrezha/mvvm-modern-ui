plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

/*android {
    namespace = "co.id.rezha.wishlist"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}*/

apply(from = rootProject.file("common-android-library.gradle"))

android {
    namespace = "co.id.rezha.wishlist"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))

    // Test dependencies
    testApi(libs.junit)
    testApi(libs.kotlin.test.junit)

    // If you're using Android instrumented tests
    androidTestApi(libs.androidx.junit.v130)
    androidTestApi(libs.androidx.espresso.core)

}