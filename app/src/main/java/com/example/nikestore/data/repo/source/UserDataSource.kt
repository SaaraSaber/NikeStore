package com.example.nikestore.data.repo.source

import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.TokenResponse
import io.reactivex.Single

interface UserDataSource {
    fun login(username: String, password: String): Single<TokenResponse>
    fun signUp(username: String, password: String): Single<MessageResponse>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
    fun saveUserName(username: String)
    fun getUserName(): String
    fun signOut()
}