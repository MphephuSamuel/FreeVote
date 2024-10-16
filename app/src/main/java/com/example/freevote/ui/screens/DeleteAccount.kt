package com.example.freevote.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccount(
    navController: NavController,
    viewModel: MainViewModel // Pass the ID number to the screen
) {
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val idNumber = viewModel.idNumber // Fetch the ID number from the ViewModel
    Log.d("DeleteAccount", "ID number fetched from ViewModel: $idNumber")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Delete Account",
            color = Color.Black,
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp) // Space below the title
        )

        // Password input (PIN)
        TextField(
            value = password.value,
            onValueChange = { newValue ->
                // Limit to 6 digits and allow only numeric input
                if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                    password.value = newValue
                }
            },
            label = { Text("Enter your PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF1A911)
            )
        )

        Spacer(modifier = Modifier.height(14.dp)) // Space between the TextField and Button

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
                            Toast.makeText(context, "Re-authentication failed. Please check your PIN.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter your PIN.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Change button color
        ) {
            Text(text = "Delete Account", color = Color.White) // Change button text color
        }
    }
}

// Function to delete user from both Firebase Authentication and Firebase Realtime Database
private fun deleteUser(user: FirebaseUser, navController: NavController, context: android.content.Context, idNumber: String?) {
    if (idNumber != null) {
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
    } else {
        Toast.makeText(context, "User ID not found. Cannot delete account.", Toast.LENGTH_SHORT).show()
    }
}
