package com.sol.jph.ui.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.model.Photo
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private var page = 1
    var limit = 20
    var albumId: Int? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val photos = MutableLiveData<List<Photo>?>()

    fun getPhoto(){
        uiScope.launch {
            println("page $page limit $limit album id $albumId")

            _isLoading.value = true

            val listOfPhotos = albumId?.let { apiRepo.getAlbumPhotos(it, page, limit) }
            if(listOfPhotos!!.isNotEmpty()){
                page += 1

                photos.postValue(listOfPhotos)

                _isLoading.value = false
            }else{
                _isLoading.value = false
            }
        }
    }
}