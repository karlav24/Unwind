import java.util.Properties
import java.io.FileInputStream
import java.io.FileNotFoundException

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.unwind"
    compileSdk = 34

    buildFeatures{
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.unwind"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["appAuthRedirectScheme"] = "unwindapp"
        // Load the Spotify credentials
        val secretsPropertiesFile = rootProject.file("secrets.properties")
        if (secretsPropertiesFile.exists()) {
            val secretsProperties = Properties().apply {
                load(secretsPropertiesFile.inputStream())
            }

            // Access properties and assign them to fields in the BuildConfig
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${secretsProperties["SPOTIFY_CLIENT_ID"]}\"")
            buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${secretsProperties["SPOTIFY_CLIENT_SECRET"]}\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI", "\"${secretsProperties["SPOTIFY_REDIRECT_URI"]}\"")
            buildConfigField("String", "OPENAI_SECRET_KEY", "\"${secretsProperties["OPENAI_SECRET_KEY"]}\"")
        } else {
            throw FileNotFoundException("Could not find secrets.properties file at ${secretsPropertiesFile.absolutePath}")
        }

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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("net.openid:appauth:0.11.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Converter for JSON
    implementation("com.squareup.okhttp3:okhttp:4.9.0") // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0") // Logging interceptor
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.security:security-crypto:1.0.0")

}