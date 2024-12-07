package com.example.gameproject.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gameproject.R
import com.example.gameproject.model.AuthManager
import com.example.gameproject.model.LoginData
import com.example.gameproject.navigation.gameprojectDestinations

class LoginViewmodel : ViewModel() {
    var passwordVisible by mutableStateOf(false)
    var loginData by mutableStateOf(LoginData("", ""))

    fun iconPassword(): Int {
        return if (passwordVisible) {
            R.drawable.baseline_visibility_24
        } else {
            R.drawable.baseline_visibility_off_24
        }
    }


    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun login(navHostController: NavHostController) {
        AuthManager.login(loginData, navHostController)
    }

    fun loginInputEmpty(): Boolean {
        return loginData.email.isEmpty() || loginData.password.isEmpty()
    }


    fun goHome(navHostController: NavHostController) {
        navHostController.navigate(gameprojectDestinations.Home.route)
    }

    fun goToRegister(navHostController: NavHostController) {
        navHostController.navigate(gameprojectDestinations.Register.route)
    }
}