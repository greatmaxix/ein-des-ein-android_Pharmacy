package com.pulse.components.notifications.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class NotificationsLocalDataSource(private val dataStore: DataStore<Preferences>)