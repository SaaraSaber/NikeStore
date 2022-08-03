package com.example.nikestore.feature.main.profile

import com.example.nikestore.NikeViewModel
import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.repo.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : NikeViewModel() {
    val userName: String
        get() = userRepository.getUserName()

    val isSignIn: Boolean
        get() = TokenContainer.token != null

    fun signOut() = userRepository.signOut()
}