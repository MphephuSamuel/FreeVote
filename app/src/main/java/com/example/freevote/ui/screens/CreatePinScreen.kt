package com.example.freevote.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.security.MessageDigest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    idNumber: String,
    lastName: String,
    names: String,
    phoneNumber: String,
    theEmail: String,
    gender: String,
    address: String,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val email = theEmail.trim()
    Spacer(modifier = Modifier.height(30.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
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

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "REGISTRATION",
                    color = Color.Red,
                    fontSize = 33.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = viewModel.pinChange,
                    onValueChange = { newValue ->
                        viewModel.updatePinChange(newValue.filter { it.isDigit() }.take(6))
                    },
                    label = { Text("Change Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = viewModel.confirm,
                    onValueChange = { newValue ->
                        viewModel.updateConfirmPin(newValue.filter { it.isDigit() }.take(6))
                    },
                    label = { Text("Confirm Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(200.dp))
                Row {
                    Button(
                        onClick = {
                            navController.navigate("registrationScreen")
                        },
                        modifier = Modifier
                            .height(30.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(200.dp))
                    Button(
                        onClick = {
                            // Check if the two PIN entries match
                            if (viewModel.pinChange == viewModel.confirm) {

                                // PIN validation (optional): Firebase requires the password/PIN to be at least 6 characters long
                                if (viewModel.confirm.length < 6) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "PIN must be at least 6 digits",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                }

                                // Register the user in Firebase Authentication with email and PIN
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, viewModel.confirm)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // If successful, store the user in the Realtime Database
                                            storeUserInRealtimeDb(
                                                idNumber, lastName, names, phoneNumber, email, gender, address
                                            )

                                            // Navigate to the home screen
                                            navController.navigate("homenews")
                                        } else {
                                            // If there was an error (e.g., email already exists or badly formatted), show the error
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Error: ${task.exception?.message}",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                            } else {
                                // Show a snackbar if the PINs do not match
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "PINs do not match",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .height(30.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward, contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Voting Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        )
    }

    SnackbarHost(hostState = snackbarHostState)
}

private fun hashPin(pin: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(pin.toByteArray())
    return hash.joinToString("") { String.format("%02x", it) }
}

fun storeUserInRealtimeDb(
    userId: String, lastName: String, names: String, phoneNumber: String,
    email: String, gender: String, address: String
) {
    val userData = hashMapOf(
        "id" to userId,
        "last name" to lastName,
        "names" to names,
        "phone number" to phoneNumber,
        "email" to email,
        "gender" to gender,
        "address" to address,
    )

    realtimeDb.child("users").child(userId)
        .setValue(userData)
        .addOnSuccessListener {
            println("User stored in Realtime DB.")
        }
        .addOnFailureListener { e ->
            println("Error storing user in Realtime DB: $e")
        }
}