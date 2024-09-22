@file:Suppress("DEPRECATION")

package com.example.freevote.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import kotlinx.coroutines.launch
import java.security.MessageDigest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePinScreen(modifier: Modifier = Modifier, navController: NavController, idNumber: String,
                    lastName: String, names: String, phoneNumber: String,
                    email: String, gender: String, address: String
) {
    val pinChange = remember { mutableStateOf("") }
    val confirm = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(30.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Padding for the Column layout
    ) {
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

        // Card wraps the form fields and text elements
        Card(
            modifier = modifier
                .fillMaxWidth() // Card fills the available width
                .padding(10.dp), // Optional padding for the Card
            shape = RoundedCornerShape(15.dp) // Optional rounded corners for the Card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Padding inside the Card for better layout
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
                    value = pinChange.value,
                    onValueChange = { newValue ->
                        pinChange.value = newValue.filter { it.isDigit() }.take(6)
                    },
                    label = { Text("Change Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation() // Add this line
                )
                val pinValue = pinChange.value
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = confirm.value,
                    onValueChange = { newValue ->
                        confirm.value = newValue.filter {it.isDigit() }.take(6) // Changed to 6
                    },
                    label = { Text("Confirm Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation() // Added this line
                )
                val confirmPinValue = confirm.value
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
                            if (pinValue == confirmPinValue) {
                                val hashedPin = hashPin(confirmPinValue) // Hash the PIN
                                storeUserInRealtimeDb(idNumber, lastName, names, phoneNumber, email, gender, address, hashedPin) //Store the hashed PIN
                                navController.navigate("homeScreen")
                            }
                            else {
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
        // Spacer for separation between Card and Image


        // The Image is clearly outside the Card
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Voting Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Adjust height as needed
                .padding(8.dp) // Optional padding around the image
        )
    }
    SnackbarHost(hostState = snackbarHostState)

}

// Function to store user credentials in Realtime Database
fun storeUserInRealtimeDb(userId: String, lastName : String, names : String, phoneNumber: String,
                          email: String, gender : String, address : String, hashedPin : String ){
    val UserData = hashMapOf(
        "id" to userId,
        "last name" to lastName,
        "names" to names,
        "phone number" to phoneNumber,
        "email" to email,
        "gender" to gender,
        "address" to address,
        "pin" to hashedPin
    )

    realtimeDb.child("users").child(userId)
        .setValue(UserData)
        .addOnSuccessListener {
            println("User stored in Realtime DB. Checking Firestore for validation...")
        }
        .addOnFailureListener { e ->
            println("Error storing user in Realtime DB: $e")
        }
}

private fun hashPin(pin: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(pin.toByteArray())
    return hash.joinToString("") { String.format("%02x", it) }
}

data class UserData(
    val id: String,
    val lastName: String,
    val names: String,
    val phoneNumber: String,
    val email: String,
    val gender: String,
    val address: String,
    val pin: String
)

/*private fun hashPin(pin: String): String {
    val argon2 = Argon2Factory.create()
    // Adjust parameters as necessary (iterations, memory, parallelism)
    val hash = argon2.hash(10, 65536, 1, pin.toCharArray())
    return hash

=======
}*/

