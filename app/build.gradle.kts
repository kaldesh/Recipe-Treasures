plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // قم بتغيير Kapt إلى KSP للحصول على أداء أفضل
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.recipetreasures"
    // compileSdk أعلى لضمان التوافق مع أحدث الميزات
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.recipetreasures"
        minSdk = 24
        targetSdk = 36 // targetSdk أعلى لأحدث تحسينات الأمان والأداء
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
        // استخدم JavaVersion.VERSION_1_8 إذا كنت تستهدف إصدارات أقدم من الأندرويد
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX + Material
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler) // تم تغيير kapt إلى ksp هنا
    implementation(libs.androidx.room.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Glide
    implementation(libs.glide)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}