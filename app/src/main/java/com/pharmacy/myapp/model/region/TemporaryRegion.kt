package com.pharmacy.myapp.model.region

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temporary_region")
data class TemporaryRegion(@PrimaryKey val id: Int, val name: String)