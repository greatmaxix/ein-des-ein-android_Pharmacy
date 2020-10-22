package com.pulse.model.region

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_region")
data class LocalRegion(@PrimaryKey val id: Int, val name: String)