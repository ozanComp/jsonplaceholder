package com.sol.jph.api

import com.sol.jph.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/posts")
    suspend fun getPosts(@Query("_page") page: Int, @Query("_limit") limit : Int): List<Post>

    @GET("/users")
    suspend fun getUser(@Query("id") userId: Int): List<User>

    @GET("/comments")
    suspend fun getPostComments(@Query("postId", ) postId: Int, @Query("_page") page: Int,
                        @Query("_limit") limit : Int): List<Comment>

    @GET("/albums")
    suspend fun getUserAlbums(@Query("userId") userId: Int, @Query("_page") page: Int,
                              @Query("_limit") limit : Int): List<Album>

    @GET("/photos")
    suspend fun getAlbumPhotos(@Query("albumId") albumId: Int, @Query("_page") page: Int,
                               @Query("_limit") limit : Int): List<Photo>
}