package com.sol.jph.ui.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.jph.dao.UserDatabaseRepository
import com.sol.jph.model.PostInfo
import com.sol.jph.api.ApiRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostViewModel @Inject constructor(private val apiRepo: ApiRepo, private val userDatabaseRepository: UserDatabaseRepository)
    : ViewModel(), Observable{

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    private var page = 1
    private var limit = 5

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val postList = MutableLiveData<List<PostInfo>>()
    private var localList =  mutableListOf<PostInfo>()

    fun getPosts() {
        uiScope.launch {
            println("page $page limit $limit")

            _isLoading.value = true

            val listOfPosts = apiRepo.getPosts(page, limit)

            if(listOfPosts.isNotEmpty()){
                page += 1

                val listOfPostInfo = mutableListOf<PostInfo>()
                listOfPosts.forEach {
                    val postInfo = PostInfo()
                    val user = apiRepo.getUser(it.userId).first()

                    postInfo.post = it
                    postInfo.user = user

                    listOfPostInfo.add(postInfo)
                }

                localList.addAll(listOfPostInfo)
                postList.postValue(localList.toList())

                _isLoading.value = false
            }else{
                page -= 1
                _isLoading.value = false
            }
        }
    }
}