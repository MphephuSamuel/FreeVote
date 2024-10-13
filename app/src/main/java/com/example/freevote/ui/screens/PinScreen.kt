package com.example.freevote.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinScreen(navController: NavController,
              idNumber: String,
              viewModel: MainViewModel, // Make sure to use the correct ViewModel type
              modifier: Modifier = Modifier // Default value for modifier
)
{
    ConnectivityAlertDialog()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    // Instead of using 'remember', we use the ViewModel to store the PIN
    var pin by remember { mutableStateOf(TextFieldValue(viewModel.pinCode ?: "")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // FREEvote! title
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

        Spacer(modifier = Modifier.height(26.dp))

        // South African flag
        Image(
            painter = painterResource(id = R.drawable.flag), // Make sure the drawable exists
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black, // Black border
                    shape = RectangleShape // Rectangular shape for consistency
                )
                .padding(1.dp) // Padding between border and content
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // PIN input field with yellow background and rectangular shape
                TextField(
                    value = pin,
                    onValueChange = { newValue ->
                        // Filter out non-numeric characters and limit to 6 digits
                        if (newValue.text.all { it.isDigit() } && newValue.text.length <= 6) {
                            pin = newValue
                            // Update ViewModel with the new PIN
                            viewModel.updatePinCode(newValue.text)
                        }
                    },
                    label = { Text("PIN", color = Color.DarkGray) },
                    modifier = Modifier
                        .weight(1f)
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911), // Yellow background
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent

                    ),
                    shape = RectangleShape, // Rectangular shape to match other components
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = onClick@{

                        if (viewModel.pinCode.isEmpty()) {
                            Toast.makeText(context, "Please enter your PIN", Toast.LENGTH_SHORT).show()
                            return@onClick // Exit the onClick if PIN is    empty
                        }
                        performLoginWithPin(
                            idNumber = idNumber,
                            pin = viewModel.pinCode,
                            context = context,
                            navController = navController,
                            viewModel
                        )
                    },
                    modifier = Modifier
                        .padding(start = 1.dp) // Add space between text field and button
                        .size(57.dp), // Match the height of the TextField
                    shape = RectangleShape, // Rectangular shape
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1A911)) // Yellow background
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(150.dp) // Set the icon size
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Forget PIN? and Reset Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Forget PIN?", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    // Reset PIN
                    navController.navigate("forgotPin/$idNumber")
                    pin = TextFieldValue("")
                    viewModel.updatePinCode("") // Reset PIN in ViewModel as well
                    Toast.makeText(context, "PIN Reset", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0E7609),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(1.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Reset", modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp))
            }
        }

        // Illustrations of people voting
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
}

fun performLoginWithPin(
    idNumber: String,
    pin: String, // This is the PIN (used as the password)
    context: Context,
    navController: NavController,
    viewModel: MainViewModel
) {
    val database = FirebaseDatabase.getInstance().getReference("users/$idNumber")

    // Fetch user data from Firebase Realtime Database
    database.get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            // Extract user details
            val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
            val lastName = dataSnapshot.child("last name").getValue(String::class.java) ?: ""
            val names = dataSnapshot.child("names").getValue(String::class.java) ?: ""
            val phoneNumber = dataSnapshot.child("phone number").getValue(String::class.java) ?: ""
            val gender = dataSnapshot.child("gender").getValue(String::class.java) ?: ""
            val address = dataSnapshot.child("address").getValue(String::class.java) ?: ""

            if (email.isNotEmpty()) {
                // Use Firebase Authentication to log in with the retrieved email and entered PIN
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pin)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful, update ViewModel and navigate to the home screen
                            viewModel.updateLastName(lastName)
                            viewModel.updateNames(names)
                            viewModel.updatePhoneNumber(phoneNumber)
                            viewModel.updateEmail(email)
                            viewModel.updateGender(gender)
                            viewModel.updateAddress(address)
                            viewModel.updateIdNumber(idNumber)

                            navController.navigate("homenews")
                                // Navigate to the home screen
                        } else {
                            // Login failed, show error message
                            Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Email not found for the given ID number
                Toast.makeText(context, "No email found for this ID Number", Toast.LENGTH_SHORT).show()
            }
        } else {
            // User not found in the database
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        // Error occurred while fetching data from Firebase
        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
    }
}

