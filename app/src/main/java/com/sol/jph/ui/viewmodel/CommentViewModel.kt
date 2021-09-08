package com.sol.jph.ui.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.model.Comment
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable {

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private var page = 1
    private var limit = 5
    var postId: Int? = null

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val commentList = MutableLiveData<List<Comment>?>()
    private var localList =  mutableListOf<Comment>()

    fun getComment() {
        uiScope.launch {
            println("page $page limit $limit")

            _isLoading.value = true

            val listOfComments = postId?.let { apiRepo.getComments(it, page, limit) }

            if(listOfComments!!.isNotEmpty()){
                page += 1

                localList.addAll(listOfComments.toTypedArray())
                commentList.postValue(localList.toList())

                _isLoading.value = false
            }else{
                _isLoading.value = false
            }
        }
    }
}