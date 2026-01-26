plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.devtools.ksp) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" // Use latest version
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

apply(from = rootProject.file("common-android-library.gradle"))

android {
    namespace = "co.id.rezha.core"
    buildFeatures {
        viewBinding = true
    }

}


dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)

    api(libs.androidx.activity)
    api(libs.androidx.constraintlayout)
    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)

    // database - CORRECTED
    api("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    api("androidx.room:room-ktx:2.6.1")
    api("androidx.room:room-paging:2.6.1")
    // Paging 3.0
    api("androidx.paging:paging-compose:1.0.0-alpha15")

//    api(libs.androidx.room.runtime)
//    api(libs.androidx.room.ktx)
//    ksp(libs.androidx.room.compiler)

    //Dagger - Hilt
    api(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    api(libs.converter.gson)
//    api(libs.glide)
}