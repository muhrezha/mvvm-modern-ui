plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply(from = rootProject.file("common-android-library.gradle"))

android {
    namespace = "co.id.rezha.mycompose"
    // Ganti viewBinding dengan compose
    buildFeatures {
        compose = true
        // HAPUS: viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))

    // Compose BOM (Bill of Materials)
    api(platform(libs.androidx.compose.bom))

    // Compose dependencies
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.activity.compose)
    api(libs.androidx.lifecycle.runtime.ktx)

    // Debug dependencies
    debugApi(libs.androidx.compose.ui.tooling)
    debugApi(libs.androidx.compose.ui.test.manifest)

    // Testing
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.compose.ui.test.junit4)

    // Core Android dependencies (jika masih diperlukan)
    api(libs.androidx.appcompat)
    api(libs.material)

    // Test dependencies
    testApi(libs.junit)
    testApi(libs.kotlin.test.junit)

    // If you're using Android instrumented tests
    androidTestApi(libs.androidx.junit.v130)
    androidTestApi(libs.androidx.espresso.core)

}