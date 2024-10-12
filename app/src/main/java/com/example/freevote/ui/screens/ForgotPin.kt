package com.example.freevote.ui.screens

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

        // Email Input Field
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier.height(57.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
            shape = RoundedCornerShape(0.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Send Re-authentication Email Button
        Button(
            onClick = {
                // Send a password reset email for re-authentication
                sendPasswordResetEmail(email.value.text, context)
                navController.navigate("pinScreen/$idNumber")
            },
            modifier = Modifier.fillMaxWidth().height(57.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
        ) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Illustrations of people voting
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Voting Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .size(500.dp)
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

