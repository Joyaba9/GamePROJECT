package com.example.gameproject.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gameproject.ui.home.HomePageViewModel

@Composable
fun HomePageView(
    navController: NavHostController,
    viewModel: HomePageViewModel = viewModel()
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // For demonstration, we have only two difficulty levels: "easy" and "hard"
        val difficultyOptions = listOf("easy", "hard")
        val selectedDifficulty by remember { viewModel.selectedDifficulty }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Welcome to the Coding Kids Game!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Select a Difficulty:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Radio buttons for difficulty selection
            difficultyOptions.forEach { difficulty ->
                val isSelected = (difficulty == selectedDifficulty)
                DifficultyOption(
                    difficulty = difficulty,
                    selected = isSelected,
                    onSelect = { viewModel.onDifficultySelected(difficulty) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.startGame(navController) },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Start New Game")
            }
        }
    }
}

@Composable
fun DifficultyOption(
    difficulty: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    // A simple row with a radio button and the difficulty label
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = difficulty.replaceFirstChar { it.uppercaseChar() },
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
