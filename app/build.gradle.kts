plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.metercalc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.metercalc"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        compose = true  // Enables Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion ="1.5.14"  // Use the latest version
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose BOM (Manages compatible versions)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Core Jetpack Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material") // ✅ Fixes MaterialTheme & Surface
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")

    // ViewModel support for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Debugging tools (Optional)
    debugImplementation("androidx.compose.ui:ui-tooling")
}