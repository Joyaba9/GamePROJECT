package com.example.gameproject.model

import com.example.gameproject.navigation.gameprojectDestinations

sealed class AuthState(val destination: gameprojectDestinations? = null) {
    object Authenticated : AuthState( gameprojectDestinations.Home)
    object Unauthenticated : AuthState( gameprojectDestinations.Login)
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}