package com.sol.jph.ui.viewmodel

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.sol.jph.dao.UserDatabaseEntity
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var userEntity = MutableLiveData<UserDatabaseEntity>()

    @Bindable
    val inputFirstName = MutableLiveData<String>()

    @Bindable
    val inputLastName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()

    @Bindable
    val inputUserName = MutableLiveData<String>()

    private val _navigateTo = MutableLiveData<Boolean>()
    val navigateTo: LiveData<Boolean>
        get() = _navigateTo

    private val _errorToast = MutableLiveData<Boolean>()
    val errorToast: LiveData<Boolean>
        get() = _errorToast

    private val _errorToastEmail = MutableLiveData<Boolean>()
    val errorToastEmail: LiveData<Boolean>
        get() = _errorToastEmail

    private val _errorToastUser = MutableLiveData<Boolean>()
    val errorToastUser: LiveData<Boolean>
        get() = _errorToastUser

    fun doneNavigating() {
        _navigateTo.value = false
    }

    fun doneToast() {
        _errorToast.value = false
    }

    fun doneToastEmail() {
        _errorToastEmail.value = false
    }

    fun doneToastErrorUser() {
        _errorToastUser.value = false
    }

    fun signIn(){
        if (inputFirstName.value == null || inputLastName.value == null || inputEmail.value == null || inputPassword.value == null || inputUserName.value == null) {
            _errorToast.value = true
        } else if(! Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            _errorToastEmail.value = true
        } else {
            uiScope.launch {
                val usersNames = userDatabaseRepository.getUser(inputEmail.value!!)
                if (usersNames != null) {
                    _errorToastUser.value = true
                } else {
                    val firstName = inputFirstName.value!!
                    val lastName = inputLastName.value!!
                    val email = inputEmail.value!!
                    val password = inputPassword.value!!
                    val userName = inputUserName.value!!

                    userEntity.value = UserDatabaseEntity(0, email, userName, firstName, lastName, password, 1)
                    insert(userEntity.value!!)

                    inputFirstName.value = null
                    inputLastName.value = null
                    inputEmail.value = null
                    inputPassword.value = null
                    inputUserName.value = null

                    _navigateTo.value = true
                }
            }
        }
    }

    private fun insert(userDatabase: UserDatabaseEntity): Job = viewModelScope.launch {
        userDatabaseRepository.insert(userDatabase)
    }
}