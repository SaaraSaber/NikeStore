package com.example.nikestore.data.repo

import io.reactivex.Completable

interface UserRepository {
    fun login(username: String, password: String): Completable
    fun signUp(username: String, password: String): Completable
    fun loadToken()
    fun getUserName(): String
    fun signOut()
}