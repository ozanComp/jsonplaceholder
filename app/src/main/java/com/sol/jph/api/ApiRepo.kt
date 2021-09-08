package com.sol.jph.api

import com.sol.jph.model.*
import javax.inject.Inject

class ApiRepo @Inject constructor(private val api: ApiService) {
    suspend fun getPosts(page: Int, limit: Int): List<Post> {
        return api.getPosts(page, limit)
    }

    suspend fun getComments(postId: Int, page: Int, limit: Int): List<Comment> {
        return api.getPostComments(postId, page, limit)
    }

    suspend fun getUser(userId: Int): List<User> {
        return api.getUser(userId)
    }

    suspend fun getUserAlbums(userId: Int, page: Int, limit: Int): List<Album> {
        return api.getUserAlbums(userId, page, limit)
    }

    suspend fun getAlbumPhotos(albumId: Int, page: Int, limit: Int): List<Photo> {
        return api.getAlbumPhotos(albumId, page, limit)
    }
}