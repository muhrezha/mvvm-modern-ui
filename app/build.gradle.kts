plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

apply(from = rootProject.file("common-android-library.gradle"))

android {
    namespace = "co.id.rezha.mycrud"

    defaultConfig {
        applicationId = "co.id.rezha.mycrud"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    /*Panggil semua module project.
             code ref: 001-roomdb*/
    implementation(project(":core"))
    implementation(project(":myroomdb"))
    implementation(project(":mycompose"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test.junit)

    // If you're using Android instrumented tests
    androidTestImplementation(libs.androidx.junit.v130)
    androidTestImplementation(libs.androidx.espresso.core)

    //Lifecycle
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Retrofit + Gson + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
//    implementation(libs.moshi)


    // Nav Drawer
    implementation(libs.duo.navigation.drawer)

    // Tabbar
    implementation(libs.androidx.viewpager2)

    // Fragments
    implementation(libs.androidx.fragment.ktx)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    implementation(libs.glide)

}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}
