package com.example.freevote.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

val firestoreDb = FirebaseFirestore.getInstance()
// Firestore (Home Affairs)
val realtimeDb = FirebaseDatabase.getInstance().reference // Realtime Database (User Credentials)

// Function to store user credentials in Realtime Database
fun storeUserInRealtimeDb(userId: String, lastName : String, names : String, gender : String ){
    val userData = hashMapOf(
        "id" to userId,
        "last name" to lastName,
        "names" to names,
        "gender" to gender
    )

    realtimeDb.child("users").child(userId)
        .setValue(userData)
        .addOnSuccessListener {
            println("User stored in Realtime DB. Checking Firestore for validation...")
        }
        .addOnFailureListener { e ->
            println("Error storing user in Realtime DB: $e")
        }
}

// Function to validate user in Firestore (Home Affairs)
fun validateUserInHomeAffairs(userId: String, lastName : String, names : String, gender : String) {
    firestoreDb.collection("citizens").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val storedLastName = document.getString("last name")
                val storedNames = document.getString("names")
                val storedGender = document.getString("gender")
                if (storedLastName == lastName && storedNames == names && storedGender == gender) {
                    println("User validated successfully in Home Affairs DB")
                    // User is valid, allow voting or other actions
                } else {
                    println("Validation failed: Details Mismatch at home Affairs DB")
                    // Name does not match, show an error message
                }
            } else {
                println("User not found in Home Affairs DB")
                // User not found in Firestore (Home Affairs), show an error message
            }
        }
}

// Function to display an error message
fun showErrorMessage(message: String) {
    // You can modify this to display an actual error message in the UI
    println("Error: $message")
}


