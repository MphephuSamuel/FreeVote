package com.example.freevote.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPinScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    idNumber: String,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue()) }
    val isPinUpdated = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

        // Email Input Field
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
                // Email TextField with yellow background and rectangular shape
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email Address", color = Color.DarkGray) },
                    modifier = Modifier
                        .weight(1f)
                        .height(57.dp), // Match the height of the TextField
                    shape = RectangleShape, // Rectangular shape for consistency
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911), // Yellow background
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )

                Button(
                    onClick = {
                        // Send a password reset email for re-authentication
                        sendPasswordResetEmail(email.value.text, context)
                        navController.navigate("pinScreen/$idNumber")
                    },
                    modifier = Modifier
                        .padding(start = 1.dp) // Space between TextField and Button
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

// Function to send password reset email
private fun sendPasswordResetEmail(email: String, context: android.content.Context) {
    val firebaseAuth = FirebaseAuth.getInstance()

    firebaseAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

