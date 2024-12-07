package com.example.gameproject.ui.splash

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gameproject.model.AuthState
import com.example.gameproject.navigation.gameprojectDestinations
import kotlinx.coroutines.delay

class SplashViewModel(): ViewModel() {
    suspend fun waitAndNavigate(navHostController: NavHostController, authState: AuthState) {
        delay(2000)
        when (authState) {
            is AuthState.Authenticated -> navHostController.navigate(
                gameprojectDestinations.Home.route
            )
            else -> navHostController.navigate(
                gameprojectDestinations.Login.route
            )
        }
    }
}