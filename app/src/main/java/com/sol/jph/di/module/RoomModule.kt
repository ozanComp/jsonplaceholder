package com.sol.jph.di.module

import android.app.Application
import androidx.room.Room
import com.sol.jph.dao.UserDatabase
import com.sol.jph.dao.UserDatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(_application: Application) {
    private var application = _application
    private lateinit var userDatabase: UserDatabase

    @Singleton
    @Provides
    fun provideUserDatabase(): UserDatabase {
        userDatabase = Room.databaseBuilder(application, UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
        return userDatabase
    }

    @Singleton
    @Provides
    fun provideUserDatabaseRepository(userDatabase: UserDatabase): UserDatabaseRepository {
        return UserDatabaseRepository(userDatabase)
    }
}