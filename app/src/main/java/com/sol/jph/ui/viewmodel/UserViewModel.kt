package com.sol.jph.ui.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.model.Album
import com.sol.jph.model.User
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private var page = 1
    var limit = 20
    var userId: Int? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val user = MutableLiveData<User>()
    val albums = MutableLiveData<List<Album>?>()

    fun getUser(){
        uiScope.launch {
            val mUser = userId?.let { apiRepo.getUser(it) }
            if(mUser!!.isNotEmpty()){
                user.postValue(mUser.first())
                getUserAlbum()
            }
        }
    }

    fun getUserAlbum(){
        uiScope.launch {
            println("page $page limit $limit user id $userId")

            _isLoading.value = true

            val mAlbums = userId?.let { apiRepo.getUserAlbums(it, page, limit) }
            if(mAlbums!!.isNotEmpty()){
                page += 1

                albums.postValue(mAlbums)

                _isLoading.value = false
            }else{
                _isLoading.value = false
            }
        }
    }
}