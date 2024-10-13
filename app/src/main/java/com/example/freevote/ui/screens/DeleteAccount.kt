package com.example.freevote.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccount(
    navController: NavController,
    idNumber: String // Pass the ID number to the screen
) {
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Delete Account", color = androidx.compose.ui.graphics.Color.Red)

        // Password input
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Enter your password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFFF1A911)
            )
        )

        // Button to delete account
        Button(
            onClick = {
                if (currentUser != null && password.value.isNotEmpty()) {
                    // Re-authenticate user using password
                    val credential = EmailAuthProvider.getCredential(currentUser.email!!, password.value)
                    currentUser.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                        if (reAuthTask.isSuccessful) {
                            // Re-authentication successful, proceed to delete user
                            deleteUser(currentUser, navController, context, idNumber)
                        } else {
                            // Re-authentication failed
                            Toast.makeText(context, "Re-authentication failed. Please check your password.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter your password.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Delete Account")
        }
    }
}

// Function to delete user from both Firebase Authentication and Firebase Realtime Database
private fun deleteUser(user: FirebaseUser, navController: NavController, context: android.content.Context, idNumber: String) {
    val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(idNumber) // Use idNumber here

    // First delete user data from Firebase Realtime Database
    databaseRef.removeValue().addOnCompleteListener { dbTask ->
        if (dbTask.isSuccessful) {
            // After deleting user data, delete user from Firebase Authentication
            user.delete().addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    Toast.makeText(context, "Account deleted successfully.", Toast.LENGTH_SHORT).show()
                    // Navigate to Login screen or any other screen
                    navController.navigate("idNumberScreen")
                } else {
                    // Failed to delete user from Firebase Authentication
                    Toast.makeText(context, "Failed to delete user from Firebase Authentication.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Failed to delete user data from Firebase Realtime Database
            Toast.makeText(context, "Failed to delete user data from Realtime Database.", Toast.LENGTH_SHORT).show()
        }
    }
}
