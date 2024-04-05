plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.enrech.mondly.photos.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "com.enrech.mondly.testing.MondlyAndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf(
            *freeCompilerArgs.toTypedArray(),
           "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
           "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
           "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        )
    }
}

ksp {
    arg("compose-destinations.mode", "navgraphs")
    arg("compose-destinations.moduleName", project.parent?.name.toString())
    arg("compose-destinations.codeGenPackageName", android.namespace.toString())
}

dependencies {
    //Modules
    api(project(libs.module.photos.data.get().name))
    api(project(libs.module.viewmodel.util.get().name))
    implementation(project(libs.module.core.ui.get().name))
    implementation(project(libs.module.designSystem.get().name))

    //Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material2)
    implementation(libs.io.github.raamcosta.compose.destinations)
    implementation(libs.io.github.raamcosta.compose.destinations.animations)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)
    implementation(libs.io.coil.kt.coil)
    implementation(libs.io.coil.kt.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    debugImplementation(project(libs.module.testing.get().name))

    kspAndroidTest(libs.hilt.compiler)

    //Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}