plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply(from = rootProject.file("common-android-library.gradle"))

android {
    namespace = "co.id.rezha.myroomdb"

//    resourcePrefix = "room_"

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    /*Tidak ada lib room_db, tetapi care impl gradle dari :core,
     maka project module myroomdb bisa panggil lib bawaan roomdb.
     code ref: 001-roomdb*/
    implementation(project(":core"))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Test dependencies
    testApi(libs.junit)
    testApi(libs.kotlin.test.junit)

    // If you're using Android instrumented tests
    androidTestApi(libs.androidx.junit.v130)
    androidTestApi(libs.androidx.espresso.core)
}