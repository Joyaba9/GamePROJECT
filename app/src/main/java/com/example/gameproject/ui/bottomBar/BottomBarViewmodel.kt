package com.example.gameproject.ui.bottomBar

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gameproject.navigation.gameprojectDestinations

class BottomBarViewmodel : ViewModel() {
    fun navigateToDestination(navController: NavHostController, screen: gameprojectDestinations) {
        navController.navigate(screen.route) {
            launchSingleTop = true
        }
    }
}
