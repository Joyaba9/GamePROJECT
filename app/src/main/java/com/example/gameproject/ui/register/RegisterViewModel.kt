package com.example.gameproject.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gameproject.R
import com.example.gameproject.model.AuthManager
import com.example.gameproject.model.RegisterData
import com.example.gameproject.navigation.gameprojectDestinations

class RegisterViewmodel : ViewModel() {

    var registerData by mutableStateOf(RegisterData())

    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)

    fun iconPassword(): Int {
        return if (passwordVisible) {
            R.drawable.baseline_visibility_24
        } else {
            R.drawable.baseline_visibility_off_24
        }
    }

    fun iconConfirmPassword(): Int {
        return if (confirmPasswordVisible) {
            R.drawable.baseline_visibility_24
        } else {
            R.drawable.baseline_visibility_off_24
        }
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        confirmPasswordVisible = !confirmPasswordVisible
    }

    fun register(navHostController: NavHostController) {
        // Add logic to check if age field is valid and, if under 18, parentCode is entered.
        if (registerData.age < 18 && registerData.parentCode.isEmpty()) {
            // Handle error: Child must provide parent code.
            AuthManager.setErrorState("Parent code is required for under 18.")
            return
        }

        AuthManager.register(registerData, navHostController)
    }

    fun registerInputEmpty(): Boolean {
        return registerData.email.isEmpty() || registerData.password.isEmpty()
    }

    fun goHome(navHostController: NavHostController) {
        navHostController.navigate(gameprojectDestinations.Home.route)
    }

    fun goToLogin(navHostController: NavHostController) {
        navHostController.navigate(gameprojectDestinations.Login.route)
    }

}
