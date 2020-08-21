package com.pharmacy.myapp.core.db

import androidx.annotation.WorkerThread
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update

interface BaseDao<T> {
    @WorkerThread
    @Insert(onConflict = REPLACE)
    suspend fun insert(obj: T)

    @WorkerThread
    @Insert(onConflict = REPLACE)
    suspend fun insert(obj: List<T>)

    @WorkerThread
    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg obj: T)

    @WorkerThread
    @Update(onConflict = REPLACE)
    suspend fun update(obj: T)

    @WorkerThread
    @Delete
    suspend fun delete(obj: T)
}