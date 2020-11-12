import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.apache.commons.io.output.ByteArrayOutputStream
import org.gradle.api.JavaVersion.VERSION_1_8

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
            val buildCode = "git rev-list --count remotes/origin/master remotes/origin/develop".execute.toInt()
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
            versionCode(buildCode)
            versionName(versionName + buildCode)
        }

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        android.applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "$applicationId-v.$versionName($versionCode)-$name.apk"
            }
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", true)
                arg("room.expandProjection", true)
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
                releaseNotes = "git log --pretty=format:${"%s"} -20 --merges".execute
                    .split("\n")
                    .filter { it.contains("fix/") || it.contains("feature/") || it.contains("hotfix/") }
                    .joinToString("\n") {
                        it.replace("(.*/+)", "")
                            .replace("release(.)*\n", "")
                            .replace("\"", "")
                            .replace("('.*')", "")
                    }
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
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.apache.commons:commons-lang3:3.11") // for Date extensions only
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.github.fondesa:kpermissions:3.1.2")
    implementation("com.budiyev.android:code-scanner:2.1.0")
    // Google
    implementation("com.google.android.material:material:1.2.1")
    implementation("com.google.firebase:firebase-analytics:18.0.0")
    implementation("com.google.firebase:firebase-crashlytics:17.2.2")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    // Flow
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:1.0.0-beta02")
    // AndroidX
    implementation("androidx.core:core-ktx:1.5.0-alpha05")
    implementation("androidx.browser:browser:1.3.0-rc01")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.4.0")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.2.0-beta01")
    implementation("androidx.fragment:fragment-ktx:1.3.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha09")
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
    implementation("com.github.ihsanbal:LoggingInterceptor:3.1.0") //TODO check is need
    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-runtime-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
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

val String.execute
    get() = ByteArrayOutputStream().run {
        project.exec {
            commandLine = split(" ")
            standardOutput = this@run
        }
        String(toByteArray()).trim()
    }