package com.example.nikestore.data.repo.source

import android.content.SharedPreferences
import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import io.reactivex.Single

class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signUp(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun saveUserName(username: String) {
        sharedPreferences.edit().apply {
            putString("user_name", username)
        }
            .apply()
    }

    override fun getUserName(): String = sharedPreferences.getString("user_name", "") ?: ""
    override fun signOut() {
        sharedPreferences.edit().apply {
            clear()
        }.apply()
    }
}