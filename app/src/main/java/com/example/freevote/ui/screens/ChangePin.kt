package com.example.freevote.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePinScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val currentPin = remember { mutableStateOf(TextFieldValue()) }
    val newPin = remember { mutableStateOf(TextFieldValue()) }
    val confirmPin = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // FREEvote! title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "FREE",
                fontSize = 60.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "vote",
                fontSize = 50.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "!",
                fontSize = 60.sp,
                color = Color(0xFF0E7609)
            )
        }

        // South African flag
        Image(
            painter = painterResource(id = R.drawable.flag),
            contentDescription = "South African Flag",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Current PIN Input Field
        TextField(
            value = currentPin.value,
            onValueChange = { newValue -> currentPin.value = newValue },
            label = { Text("Current PIN") },
            modifier = Modifier.height(57.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
            shape = RoundedCornerShape(0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New PIN Input Field
        TextField(
            value = newPin.value,
            onValueChange = { newValue -> newPin.value = newValue },
            label = { Text("New PIN") },
            modifier = Modifier.height(57.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
            shape = RoundedCornerShape(0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm New PIN Input Field
        TextField(
            value = confirmPin.value,
            onValueChange = { newValue -> confirmPin.value = newValue },
            label = { Text("Confirm New PIN") },
            modifier = Modifier.height(57.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
            shape = RoundedCornerShape(0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Update PIN Button
        Button(
            onClick = {
                if (newPin.value.text == confirmPin.value.text) {
                    // Call the function to update the PIN
                    updatePin(currentPin.value.text, newPin.value.text, context, navController)
                } else {
                    Toast.makeText(context, "New PINs do not match", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(57.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
        ) {
            Text("Update PIN")
        }
    }
}

// Function to update the PIN (password) using reauthentication
private fun updatePin(currentPin: String, newPin: String, context: Context, navController: NavController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser

    if (user != null) {
        // Reauthenticate user with the current PIN
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPin)

        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update the password (PIN)
                user.updatePassword(newPin).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        Toast.makeText(context, "PIN updated successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate("pinScreen") // Navigate after successful update
                    } else {
                        Toast.makeText(context, "Failed to update PIN: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Reauthentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
}
