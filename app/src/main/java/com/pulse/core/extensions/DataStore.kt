package com.pulse.core.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

fun <T> DataStore<Preferences>.getOnes(key: Preferences.Key<T>) = runBlocking { get(key).first() }

fun <T> DataStore<Preferences>.getOnes(key: Preferences.Key<T>, defaultValue: T) = runBlocking { get(key, defaultValue).first() }

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultValue: T) = data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[key] ?: defaultValue }

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>) = data
    .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
    .map { it[key] }

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { it[key] = value }
}

suspend fun <T> DataStore<Preferences>.clear(key: Preferences.Key<T>) {
    edit { it.remove(key) }
}

suspend fun DataStore<Preferences>.clearAll() {
    edit { it.clear() }
}