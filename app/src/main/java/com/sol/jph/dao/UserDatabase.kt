package com.sol.jph.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDatabaseEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDatabaseDao
}