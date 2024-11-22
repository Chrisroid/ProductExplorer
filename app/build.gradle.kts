plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.productexplorer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.productexplorer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.navigation.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Retrofit for networking
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    testImplementation(libs.mockwebserver)

    // Moshi for JSON parsing (optional, replace with Gson if preferred)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    kapt(libs.moshi.kotlin.codegen)

    // Gson for JSON parsing (if you prefer Gson over Moshi)
     implementation(libs.gson)
     implementation(libs.converter.gson)

    // Room for local database
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Debug dependencies for Room
    kaptTest (libs.androidx.room.compiler)

    // Kotlin Coroutines and Flow
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle components for ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Testing
    testImplementation(libs.junit)
    // Mocking and Flow Testing
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.app.turbine)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    testImplementation (libs.androidx.core.testing)

    //Coroutine Test
    testImplementation(libs.kotlinx.coroutines.test)

    // Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler.v248)

    //Glide
    implementation( libs.glide)

    //Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    //Shimmer Animation
    implementation(libs.shimmer)

    testImplementation (libs.kotest.assertions.core)
    testImplementation(libs.mockk)
    testImplementation (libs.robolectric)

}

kapt {
    correctErrorTypes = true
}