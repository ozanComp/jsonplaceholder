package com.sol.jph.ui.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.api.ApiRepo
import com.sol.jph.dao.UserDatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToDetail = MutableLiveData<Boolean>()
    val navigateToDetail: LiveData<Boolean>
        get() = _navigateToDetail

    fun doneNavigatetoLogin() {
        _navigateToLogin.value = false
    }

    fun doneNavigateToDetail() {
        _navigateToDetail.value = false
    }

    fun getLoggedUser(){
        uiScope.launch {
            val user = userDatabaseRepository.getLoggedUser()
            if(user != null){
                _navigateToDetail.value = true
            }else{
                _navigateToLogin.value = true
            }
        }
    }
}