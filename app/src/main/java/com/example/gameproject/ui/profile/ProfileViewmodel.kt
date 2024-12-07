package com.example.gameproject.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.gameproject.model.AuthManager
import com.example.gameproject.navigation.gameprojectDestinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProfileViewmodel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var username by mutableStateOf("")
    var role by mutableStateOf("")
    var parentCode by mutableStateOf("")

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        username = document.getString("username") ?: ""
                        role = document.getString("role") ?: ""
                        if (role == "parent") {
                            parentCode = document.getString("parentCode") ?: ""
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // handle error if needed
                }
        }
    }

    fun logout(navHostController: NavHostController) {
        AuthManager.logout()
        navHostController.navigate(gameprojectDestinations.Login.route)
    }
}
