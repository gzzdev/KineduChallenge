import java.util.Properties
val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())
val privateApiKey = localProperties.getProperty("PrivateKey")
val publicApiKey = localProperties.getProperty("PublicKey")
val baseUrl = localProperties.getProperty("BaseApi")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.cuzztomgdev.kineduchallenge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cuzztomgdev.kineduchallenge"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {


            buildConfigField("String", "PRIVATE_API_KEY", "\"${privateApiKey}\"")
            buildConfigField("String", "PUBLIC_API_KEY", "\"${publicApiKey}\"")
            buildConfigField("String", "BASE_API", "\"${baseUrl}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "PRIVATE_API_KEY", "\"${privateApiKey}\"")
            buildConfigField("String", "PUBLIC_API_KEY", "\"${publicApiKey}\"")
            buildConfigField("String", "BASE_API", "\"${baseUrl}\"")
        }
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Nav
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.interceptor)

    //Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}