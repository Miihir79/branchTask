package com.mihir.branchtask.network

import com.mihir.branchtask.model.Message
import com.mihir.branchtask.model.MessageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {

    @POST("/api/login")
    suspend fun getLoginCredentials(
        @Query("username") email: String,
        @Query("password") password: String
    ): Response<Map<String, String>>

    @GET("/api/messages")
    suspend fun getMessages(
        @Header("X-Branch-Auth-Token") authToken: String
    ): Response<Message>

    @POST("/api/messages")
    suspend fun sendMessage(
        @Header("X-Branch-Auth-Token") authToken:String,
        @Query("thread_id") threadId: Int,
        @Query("body") message: String
    ): Response<MessageItem>

    @POST("/api/reset")
    suspend fun resetApi(@Header("X-Branch-Auth-Token") authToken:String)
}