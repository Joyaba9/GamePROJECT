package com.example.gameproject.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.gameproject.navigation.gameprojectDestinations

class HomePageViewModel : ViewModel() {
    // Holds the currently selected difficulty level.
    // Could be "easy", "medium", "hard", etc. For simplicity, let's do "easy" and "hard".
    var selectedDifficulty: MutableState<String> = mutableStateOf("easy")

    fun onDifficultySelected(difficulty: String) {
        selectedDifficulty.value = difficulty
    }

    fun startGame(navController: NavHostController) {
        // Navigate to the appropriate game screen based on the selected difficulty
        when (selectedDifficulty.value) {
            "easy" -> navController.navigate(gameprojectDestinations.EasyGame.route)
         //   "hard" -> navController.navigate(gameprojectDestinations.HardGame.route)
        //    else -> navController.navigate(gameprojectDestinations.EasyGame.route) // default fallback
        }
    }
}
