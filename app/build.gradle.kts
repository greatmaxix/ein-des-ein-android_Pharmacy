import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn
import io.github.rockerhieu.versionberg.Git.getCommitCount
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(from = "${project.rootDir}/script/experimentalExtensions.gradle")

plugins {
    id("com.android.application")
    //id("kotlin-parcelize") // TODO uncomment in future
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("io.github.rockerhieu.versionberg")
}

with(tasks) {
    withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.OptIn"
        )
    }
    named("preBuild").dependsOn(register("generateNavArgsProguardRules", GenerateNavArgsProguardRulesTask::class))
}

versionberg {
    setMajor(DefaultConfig.versionMajor)
    setMinor(DefaultConfig.versionMinor)
    nameTemplate = "$major.$minor.${getCommitCount(gitDir)}}"
    codeTemplate = "((($major * 100) + $minor) * 100) * 100000 + $build"
}

android {
    compileSdkVersion(DefaultConfig.compileSdk)

    defaultConfig {
        applicationId = "com.pulse"

        consumerProguardFile(File(buildDir, NAVARGS_PROGUARD_RULES_PATH))

        with(DefaultConfig) {
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
        }
        versionCode = versionberg.code
        versionName = versionberg.name

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        applicationVariants.all {
            outputs.all { (this as BaseVariantOutputImpl).outputFileName = "../../apk/$applicationId-$name-$versionName($versionCode).apk" }
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
    }

    val debug = "debug"
    val qa = "qa"
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
        create(qa) {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-$qa"
            firebaseAppDistribution {
                releaseNotes = "Some text"
                testers = "developereinios@gmail.com, ivan.kovalenko13@gmail.com"
            }
        }
        getByName(debug) {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-$debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }

    // sourceSets["main"].java.srcDir("src/main/kotlin") TODO move java -> kotlin when no other work in branches
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
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:1.0.0-beta02")
    // AndroidX
    implementation("androidx.core:core-ktx:1.5.0-alpha04")
    implementation("androidx.browser:browser:1.3.0-beta01")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.5.0-beta01")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.2.0-beta01")
    implementation("androidx.fragment:fragment-ktx:1.3.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha08")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha06")
    with(Versions) {
        // Koin
        implementation("org.koin:koin-androidx-scope:$koin")
        implementation("org.koin:koin-androidx-fragment:$koin")
        implementation("org.koin:koin-androidx-viewmodel:$koin")
        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
        // REST
        implementation("com.squareup.retrofit2:retrofit:$retrofit")
        implementation("com.squareup.retrofit2:converter-gson:$retrofit")
        implementation("com.squareup.okhttp3:okhttp:$okhttp")
        implementation("com.github.ihsanbal:LoggingInterceptor:3.1.0")
        // Navigation
        implementation("androidx.navigation:navigation-ui-ktx:$navigation")
        implementation("androidx.navigation:navigation-runtime-ktx:$navigation")
        implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
        // Lifecycle
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")
        implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
        // Room
        implementation("androidx.room:room-runtime:$room")
        implementation("androidx.room:room-ktx:$room")
        kapt("androidx.room:room-compiler:$room")
        // Glide
        implementation("com.github.bumptech.glide:glide:$glide")
        kapt("com.github.bumptech.glide:compiler:$glide")
        // SDP/SSP
        implementation("com.intuit.sdp:sdp-android:$sdp")
        implementation("com.intuit.ssp:ssp-android:$sdp")

        implementation("com.github.heremaps:oksse:$oksse")
        implementation("com.kirich1409.android-notification-dsl:core:$notificationsDsl")
        implementation("com.kirich1409.android-notification-dsl:extensions:$notificationsDsl")
    }
}