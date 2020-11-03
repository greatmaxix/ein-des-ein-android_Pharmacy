import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    //id("kotlin-parcelize")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
}

apply(from = "${project.rootDir}/script/experimentalExtensions.gradle")

tasks {
    named("preBuild").dependsOn(register("generateNavArgsProguardRules", GenerateNavArgsProguardRulesTask::class))
}

android {
    compileSdkVersion(DefaultConfig.compileSdk)

    defaultConfig {
        applicationId = "com.pulse"

        consumerProguardFile(File(buildDir, NAVARGS_PROGUARD_RULES_PATH))

        with(DefaultConfig) {
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
            versionCode(versionCode)
            versionName(versionName)
        }

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        android.applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "$applicationId-v.$versionName($versionCode)-$name.apk"
            }
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    val release = "release"

    signingConfigs {
        create(release) {
            with(SigningConfigs) {
                keyAlias = alias
                keyPassword = password_key
                storePassword = password_store
                storeFile = rootProject.file(keystore)
            }
        }
    }

    buildTypes {
        getByName(release) {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
        }
        create("qa") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-qa"
            firebaseAppDistribution {
                releaseNotes = "Some text"
                testers = "developereinios@gmail.com, ivan.kovalenko13@gmail.com"
            }
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-dg"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = mutableListOf<String>().apply {
            addAll(freeCompilerArgs)
            addAll(listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.OptIn"))
        }
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.apache.commons:commons-lang3:3.11") // for Date extensions only
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.github.fondesa:kpermissions:3.1.2")
    implementation("com.budiyev.android:code-scanner:2.1.0")
    implementation("com.github.onimur:handle-path-oz:1.0.7")
    // Google
    implementation("com.google.android.material:material:1.2.1")
    implementation("com.google.firebase:firebase-analytics:18.0.0")
    implementation("com.google.firebase:firebase-crashlytics:17.2.2")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    // Flow
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:1.0.0-beta01")
    // AndroidX
    implementation("androidx.core:core-ktx:1.5.0-alpha04")
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.4.0")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.2.0-beta01")
    implementation("androidx.fragment:fragment-ktx:1.3.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha08")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha06")
    // Koin
    implementation("org.koin:koin-androidx-scope:${Versions.koin}")
    implementation("org.koin:koin-androidx-fragment:${Versions.koin}")
    implementation("org.koin:koin-androidx-viewmodel:${Versions.koin}")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    // REST
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.github.ihsanbal:LoggingInterceptor:3.1.0")
    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-runtime-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")
    // Room
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")
    // Glide
    implementation("com.github.bumptech.glide:glide:${Versions.glide}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")
    // SDP/SSP
    implementation("com.intuit.sdp:sdp-android:${Versions.sdp}")
    implementation("com.intuit.ssp:ssp-android:${Versions.sdp}")

    implementation("com.github.heremaps:oksse:${Versions.oksse}")
    implementation("com.kirich1409.android-notification-dsl:core:${Versions.notificationsDsl}")
    implementation("com.kirich1409.android-notification-dsl:extensions:${Versions.notificationsDsl}")
}