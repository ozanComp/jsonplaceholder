package com.sol.jph.dao

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users_table")
data class UserDatabaseEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "logged_status")
    var loggedStatus: Int
):Parcelable