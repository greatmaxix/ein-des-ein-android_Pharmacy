import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.apache.commons.io.output.ByteArrayOutputStream
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
}

tasks {
    named("preBuild").dependsOn(register("generateNavArgsProguardRules", GenerateNavArgsProguardRulesTask::class))
}

android {
    compileSdkVersion(DefaultConfig.compileSdk)

    defaultConfig {
        applicationId = "com.pulse"

        consumerProguardFile(File(buildDir, NAVARGS_PROGUARD_RULES_PATH))

        with(DefaultConfig) {
            //write new logic for determine buildcode!!
//          val buildCode = "git rev-list --count remote/origin/master remote/origin/develop".execute().toInt()
            val buildCode = 2022081700
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
            versionCode(buildCode)
            versionName(versionName + buildCode)
        }

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        applicationVariants.all {
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

    buildFeatures {
        viewBinding = true
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
            buildConfigField("Boolean", "DEVELOPER_SERVER", "false")
            buildConfigField("Boolean", "IHSANBAL", "false")
        }
        create("qa") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
            buildConfigField("Boolean", "DEVELOPER_SERVER", "false")
            buildConfigField("Boolean", "IHSANBAL", "false")
            versionNameSuffix = "-qa"
            firebaseAppDistribution {
                releaseNotes = "git log --pretty=format:${"%s"} -20 --merges".execute()
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
            buildConfigField("Boolean", "DEVELOPER_SERVER", "true")
            buildConfigField("Boolean", "IHSANBAL", "false")
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

    // sourceSets["main"].java.srcDir("src/main/kotlin") TODO move java -> kotlin when no other work in branches

    kotlinOptions {
        jvmTarget = VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.OptIn")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.github.fondesa:kpermissions:3.2.1")
    implementation("com.budiyev.android:code-scanner:2.1.0")
    // Google
    implementation(platform("com.google.firebase:firebase-bom:27.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.android.material:material:1.3.0")
    // Flow
    implementation("io.github.reactivecircus.flowbinding:flowbinding-appcompat:${Versions.flowBinding}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:${Versions.flowBinding}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-material:${Versions.flowBinding}")
    // AndroidX
    implementation("androidx.core:core-ktx:1.5.0-beta03")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.5.0")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.3.3")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0-beta01")
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
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.1"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
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
    implementation("com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:1.4.2")
}

fun String.execute(currentWorkingDir: File = file("./")): String {
    val byteOut = ByteArrayOutputStream()
    project.exec {
        workingDir = currentWorkingDir
        commandLine = this@execute.split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}