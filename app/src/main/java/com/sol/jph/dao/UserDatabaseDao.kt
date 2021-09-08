package com.sol.jph.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDatabaseDao {
    @Insert
    suspend fun insert(register: UserDatabaseEntity)

    @Query("SELECT * FROM users_table WHERE email LIKE :email")
    suspend fun getUser(email: String): UserDatabaseEntity?

    @Query("SELECT * FROM users_table WHERE logged_status == 1 LIMIT 1")
    suspend fun getLoggedUser(): UserDatabaseEntity?
}