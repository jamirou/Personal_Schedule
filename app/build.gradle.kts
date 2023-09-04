plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.jamirodev.agenda_online"
    compileSdk = 33


    defaultConfig {
        applicationId = "com.jamirodev.agenda_online"
        minSdk = 22
        targetSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.firebase:firebase-analytics:21.3.0")
    testImplementation("junit:junit:4.13.2")
    implementation("com.airbnb.android:lottie:6.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.hbb20:ccp:2.5.0")

    /*AUTHENTICATION FIREBASE*/
    implementation("com.google.firebase:firebase-auth:22.1.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))

//    DattaBase firebase
    implementation("com.google.firebase:firebase-database:20.2.2")

    implementation ("com.firebaseui:firebase-ui-database:8.0.0")


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}