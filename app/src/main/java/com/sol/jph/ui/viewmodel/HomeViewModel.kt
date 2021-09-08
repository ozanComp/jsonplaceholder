package com.sol.jph.ui.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.api.ApiRepo
import com.sol.jph.dao.UserDatabaseEntity
import com.sol.jph.dao.UserDatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var userEntity = MutableLiveData<UserDatabaseEntity?>()

    fun getLoggedUser(){
        uiScope.launch {
            val user = userDatabaseRepository.getLoggedUser()
            if(user != null){
                userEntity.postValue(user)
            }
        }
    }
}