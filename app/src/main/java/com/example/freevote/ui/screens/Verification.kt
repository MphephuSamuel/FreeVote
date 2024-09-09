package com.example.freevote.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

val firestoreDb = FirebaseFirestore.getInstance()
// Firestore (Home Affairs)
val realtimeDb = FirebaseDatabase.getInstance().reference // Realtime Database (User Credentials)

// Function to store user credentials in Realtime Database
fun storeUserInRealtimeDb(userId: String, userName: String) {
    val userData = hashMapOf(
        "id" to userId,
        "name" to userName
    )

    realtimeDb.child("users").child(userId)
        .setValue(userData)
        .addOnSuccessListener {
            println("User stored in Realtime DB. Checking Firestore for validation...")
            validateUserInHomeAffairs(userId, userName)  // Validate the user in Firestore after storing in Realtime DB
        }
        .addOnFailureListener { e ->
            println("Error storing user in Realtime DB: $e")
        }
}

// Function to validate user in Firestore (Home Affairs)
fun validateUserInHomeAffairs(userId: String, userName: String) {
    firestoreDb.collection("citizens").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val storedName = document.getString("name")
                if (storedName == userName) {
                    println("User validated successfully in Home Affairs DB")
                    // User is valid, allow voting or other actions
                } else {
                    println("Validation failed: Name mismatch")
                    // Name does not match, show an error message
                    showErrorMessage("Validation failed: Name mismatch.")
                }
            } else {
                println("User not found in Home Affairs DB")
                // User not found in Firestore (Home Affairs), show an error message
                showErrorMessage("User not found in Home Affairs DB.")
            }
        }
        .addOnFailureListener { e ->
            println("Error validating user in Firestore: $e")
            showErrorMessage("Error validating user: ${e.message}")
        }
}

// Function to display an error message
fun showErrorMessage(message: String) {
    // You can modify this to display an actual error message in the UI
    println("Error: $message")
}



@Composable
fun UserValidationScreen(userId: String, userName: String, navController: NavController) {
    // Call the validation function here
    storeUserInRealtimeDb(userId, userName)

    // ... rest of your UI for this screen ...
}