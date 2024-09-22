@file:Suppress("DEPRECATION")

package com.example.freevote.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.freevote.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinScreen(modifier: Modifier, idNumber: String, navController: NavController) {
    val context = LocalContext.current
    val pin = remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
                fontFamily = RubikMoonrocks,
                fontSize = 60.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "vote",
                fontFamily = RubikMoonrocks,
                fontSize = 50.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "!",
                fontFamily = RubikMoonrocks,
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

        // PIN input field with yellow background
        TextField(
            value = pin.value,
            onValueChange = { newValue ->
                // Filter out non-numeric characters
                if (newValue.text.all { it.isDigit() } && newValue.text.length <= 6) {
                    pin.value = newValue
                }
            },
            label = { Text("PIN") },
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
            shape = RoundedCornerShape(0.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (pin.value.text.isNotBlank()) {
                    verifyPinAndNavigate(pin.value.text, idNumber, navController, context, coroutineScope, snackbarHostState)
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Please enter your PIN",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1A911))
        ) {
            Text("Login")
        }

        // Forget PIN? and Reset Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Forget PIN?",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    pin.value = TextFieldValue("")
                    Toast.makeText(context, "PIN Reset", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E7609),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(1.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    "Reset",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp)
                )
            }
        }

        // Illustrations of people voting
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Voting Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .size(500.dp)
        )

        // Snackbar
        SnackbarHost(hostState = snackbarHostState)
    }
}

fun verifyPinAndNavigate(
    enteredPin: String,
    idNumber: String,
    navController: NavController,
    context: Context,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.reference.child("users").child(idNumber)

    usersRef.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val storedHashedPin = snapshot.child("pin").value as String
            val enteredHashedPin = hashPin(enteredPin)

            if (enteredHashedPin == storedHashedPin) {
                navController.navigate("homeScreen")
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Incorrect PIN",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "User not found",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }.addOnFailureListener { e ->
        println("Error verifying PIN: $e")
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = "Error verifying PIN",
                duration = SnackbarDuration.Short
            )
        }
    }
}

private fun hashPin(pin: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(pin.toByteArray())
    return hash.joinToString("") { String.format("%02x", it) }
}