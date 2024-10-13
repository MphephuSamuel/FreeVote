package com.example.freevote.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
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
    name: String,
    number: String,
    theEmail: String,
    theGender: String,
    theAddress: String,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val email = theEmail.trim()
    val names = name.trim()
    val phoneNumber =  number.trim()
    val gender = theGender.trim()
    val address = theAddress.trim()
    Spacer(modifier = Modifier.height(30.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("FREE")
                }
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("vote")
                }
                withStyle(style = SpanStyle(color = Color(0xFF006400))) {
                    append("!")
                }
            },
            fontFamily = rubikMoonrocksFont,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 48.sp,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier.padding(16.dp)
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .shadow(8.dp, shape = RoundedCornerShape(15.dp)),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    label = { Text("Create Pin") },
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { navController.navigate("registrationScreen/$idNumber") },
                        modifier = Modifier.height(30.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            // Check if the two PIN entries match
                            if (viewModel.pinChange == viewModel.confirm) {

                                // PIN validation: Firebase requires the password/PIN to be at least 6 characters long
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
                        modifier = Modifier.height(30.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(65.dp))

        Image(
            painter = painterResource(id = R.drawable.people), // Ensure the drawable exists
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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