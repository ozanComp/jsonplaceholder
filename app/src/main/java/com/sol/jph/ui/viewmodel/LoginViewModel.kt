package com.sol.jph.ui.viewmodel

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.dao.UserDatabaseEntity
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var userEntity = MutableLiveData<UserDatabaseEntity?>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()

    private val _navigateToSignIn = MutableLiveData<Boolean>()
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    private val _navigateToLoggedIn = MutableLiveData<Boolean>()
    val navigateToLoggedIn: LiveData<Boolean>
        get() = _navigateToLoggedIn

    private val _errorToast = MutableLiveData<Boolean>()
    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastEmail = MutableLiveData<Boolean>()
    val errorToastEmail: LiveData<Boolean>
        get() = _errorToastEmail

    private val _errorToastInvalidPassword = MutableLiveData<Boolean>()
    val errorToastInvalidPassword: LiveData<Boolean>
        get() = _errorToastInvalidPassword

    private val _errorToastUser = MutableLiveData<Boolean>()
    val errorToastUser: LiveData<Boolean>
        get() = _errorToastUser

    fun doneNavigatingSignIn() {
        _navigateToSignIn.value = false
    }

    fun doneNavigatingLoggedIn() {
        _navigateToLoggedIn.value = false
    }

    fun doneToast() {
        _errorToast.value = false
    }

    fun doneToastErrorEmail() {
        _errorToastEmail.value = false
    }

    fun doneToastInvalidPassword() {
        _errorToastInvalidPassword .value = false
    }

    fun doneToastErrorUser() {
        _errorToastUser.value = false
    }


    fun logIn() {
        if (inputEmail.value == null || inputPassword.value == null) {
            _errorToast.value = true
        } else if(! Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            _errorToastEmail.value = true
        } else {
            uiScope.launch {
                val user = userDatabaseRepository.getUser(inputEmail.value!!)
                if (user != null) {
                    if(user.password == inputPassword.value){
                        userEntity.value = user

                        inputEmail.value = null
                        inputPassword.value = null
                        _navigateToLoggedIn.value = true
                    }else{
                        _errorToastInvalidPassword.value = true
                    }
                } else {
                    _errorToastUser.value = true
                }
            }
        }
    }

    fun signIn() {
        _navigateToSignIn.value = true
    }
}