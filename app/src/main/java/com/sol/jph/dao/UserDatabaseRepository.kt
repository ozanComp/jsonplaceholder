package com.sol.jph.dao

class UserDatabaseRepository(userDatabase: UserDatabase) {

    private var dao:UserDatabaseDao = userDatabase.getUserDao()

    suspend fun insert(userDatabase: UserDatabaseEntity) {
        return dao.insert(userDatabase)
    }

    suspend fun getUser(email: String):UserDatabaseEntity?{
        return dao.getUser(email)
    }

    suspend fun getLoggedUser():UserDatabaseEntity?{
        return dao.getLoggedUser()
    }
}