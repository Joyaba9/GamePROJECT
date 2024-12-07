package com.example.gameproject.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.gameproject.navigation.gameprojectDestinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Authenticated
            }
        }
    }

    fun register(registerData: RegisterData, navHostController: NavHostController) {
        if (registerData.email.isEmpty() || registerData.password.isEmpty() || registerData.confirmPassword.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        if (registerData.password != registerData.confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        // If under 18, we must verify parent code first
        if (registerData.age in 1..17 && registerData.parentCode.isEmpty()) {
            _authState.value = AuthState.Error("Parent code is required for users under 18.")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(registerData.email, registerData.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    if (registerData.age >= 18) {
                        // Parent user
                        val parentCode = generateParentCode() // e.g., "P25631"
                        val userMap = mapOf(
                            "username" to registerData.username,
                            "email" to registerData.email,
                            "role" to "parent",
                            "parentCode" to parentCode,
                            "age" to registerData.age
                        )
                        db.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                                // After parent registers, navigate to login
                                navHostController.navigate(gameprojectDestinations.Login.route)
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error("Failed to save user data: ${e.message}")
                            }
                    } else {
                        // Child user: Validate the parent code
                        db.collection("users")
                            .whereEqualTo("parentCode", registerData.parentCode)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                if (querySnapshot.isEmpty) {
                                    // no parent with that code
                                    _authState.value = AuthState.Error("Invalid parent code.")
                                } else {
                                    // Assume there's only one parent with that code
                                    val parentDoc = querySnapshot.documents[0]
                                    val userMap = mapOf(
                                        "username" to registerData.username,
                                        "email" to registerData.email,
                                        "role" to "child",
                                        "age" to registerData.age,
                                        "linkedParentCode" to registerData.parentCode,
                                        "parentUid" to parentDoc.id
                                    )
                                    db.collection("users").document(userId)
                                        .set(userMap)
                                        .addOnSuccessListener {
                                            _authState.value = AuthState.Authenticated
                                            navHostController.navigate(gameprojectDestinations.Login.route)
                                        }
                                        .addOnFailureListener { e ->
                                            _authState.value = AuthState.Error("Failed to save child data: ${e.message}")
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error("Error checking parent code: ${e.message}")
                            }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun login(loginData: LoginData, navHostController: NavHostController) {
        if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(loginData.email, loginData.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    navHostController.navigate(gameprojectDestinations.Home.route)
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun setErrorState(message: String) {
        _authState.value = AuthState.Error(message)
    }

    private fun generateParentCode(): String {
        // A simple code generator.
        // In reality, you might want something more robust or unique.
        return "P" + (10000..99999).random()
    }
}
