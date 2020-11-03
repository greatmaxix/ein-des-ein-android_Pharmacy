buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("org.koin:koin-gradle-plugin:${Versions.koin}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.3.0")
        classpath("com.google.firebase:firebase-appdistribution-gradle:2.0.1")
    }
}

apply(from = "${project.rootDir}/script/apk_upload.gradle")

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}