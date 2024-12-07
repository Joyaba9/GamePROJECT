package com.example.gameproject.navigation

import com.example.gameproject.R

//
sealed class gameprojectDestinations(val route: String, val title: String = "", val icon: Int = 0) {
    data object Splash : gameprojectDestinations("splash")
    data object Login : gameprojectDestinations("login")
    data object Register : gameprojectDestinations("register")
    data object EasyGame : gameprojectDestinations("easy_game", "Easy Game")
    //data object HardGame : gameprojectDestinations("hard_game", "Hard Game")




    data object Home : gameprojectDestinations("home", "Home", R.drawable.baseline_home_filled_24)
    data object Profile : gameprojectDestinations("profile", "Profile", R.drawable.baseline_account_circle_24)
}

// Bottom bar destinations
val bottomBarDestinations = listOf(
    gameprojectDestinations.Home,
    gameprojectDestinations.Profile
)

