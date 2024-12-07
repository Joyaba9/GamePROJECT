package com.example.gameproject.ui.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var role = mutableStateOf("")
    var attempts = mutableStateOf(listOf<String>())

    init {
        loadUserRole()
    }

    private fun loadUserRole() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    val doc = db.collection("users").document(uid).get().await()
                    val userRole = doc.getString("role") ?: ""
                    role.value = userRole
                } catch (e: Exception) {
                    // If any error occurs, default to "child"
                    role.value = "child"
                }
            }
        } else {
            // No logged in user, default to "child"
            role.value = "child"
        }
    }

    fun addAttempt(result: String) {
        val updatedAttempts = attempts.value.toMutableList()
        updatedAttempts.add(result)
        attempts.value = updatedAttempts
    }
}
