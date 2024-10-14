@file:Suppress("DEPRECATION")

package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.example.freevote.viewmodel.MainViewModel
import com.example.freevote.R
import com.example.freevote.ui.screens.ConnectivityAlertDialog
import com.example.freevote.ui.screens.firestoreDb
import com.example.freevote.util.isInternetAvailable
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.*

val rubikMoonrocksFont = FontFamily(
    Font(
        resId = R.font.rubik_moonrocks, // Ensure the font file exists in res/font
        weight = FontWeight.Normal
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdNumberScreen(navController: NavHostController, viewModel: MainViewModel) {
    // Display a dialog for connectivity issues if needed
    ConnectivityAlertDialog()

    var idNumber by remember { mutableStateOf(viewModel.idNumber) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) } // Loading state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Text header
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
                    color = Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Image(
            painter = painterResource(id = R.drawable.flag), // Make sure the drawable exists
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        if (isLoading) {
            // Show CircularProgressIndicator when loading
            CircularProgressIndicator(
                color = Color(0xFFF1A911),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(60.dp)
            )
        }
        else{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black, // Black color
                        shape = RectangleShape // Rectangular shape
                    )
                    .padding(1.dp) // Padding between border and content
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // OutlinedTextField with rounded shape, shadow, and background color
                    TextField(
                        value = idNumber,
                        onValueChange = { newId ->
                            // Allow only digits and limit to 13 characters
                            if (newId.length <= 13 && newId.all { it.isDigit() }) {
                                idNumber = newId
                                viewModel.updateIdNumber(newId)
                            }
                        },
                        label = {
                            Text("ID NUMBER:", color = DarkGray)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(57.dp),
                        shape = RectangleShape, // All corners will be square
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFF1A911),
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent,
                            disabledIndicatorColor = Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )


                    Button(
                        onClick = {
                            if (idNumber.isBlank()) {
                                Toast.makeText(context, "Please enter your ID number", Toast.LENGTH_SHORT).show()
                            } else {
                                // Check internet connection before proceeding
                                if (isInternetAvailable(context)) {
                                    isLoading = true // Show loading indicator
                                    validateUserInHomeAffairs(idNumber) { isValidFirestore, id ->
                                        if (isValidFirestore) {
                                            // Hide loading indicator
                                            // Check if the user is 18 or older
                                            if (isUser18OrOlder(idNumber)) {
                                                // ID is valid in Firestore, now check Realtime Database
                                                validateUserInRealtimeDb(idNumber) { isValidRealtime ->
                                                    if (isValidRealtime) {
                                                        // ID is valid in both, proceed to pin screen
                                                        isLoading = false
                                                        navController.navigate("pinScreen/$id")
                                                    } else {

                                                        // ID is only valid in Firestore, update the ViewModel and proceed to registration screen
                                                        viewModel.updateIdNumber(idNumber) // Update ViewModel here
                                                        isLoading=false
                                                        navController.navigate("registrationScreen/$idNumber")
                                                    }
                                                    isLoading = false
                                                }
                                            } else {
                                                isLoading=false
                                                Toast.makeText(context, "You must be 18 or older to proceed.", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            isLoading=false
                                            // ID is invalid in Firestore, show an error message
                                            Toast.makeText(context, "Invalid ID number", Toast.LENGTH_SHORT).show()
                                        }
                                        isLoading=false
                                    }
                                } else {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(start = 1.dp) // Add space between text field and button
                            .size(57.dp), // Adjust size to match TextField height
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1A911))
                    ) {
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(150.dp) // Set the icon size
                        )
                    }
                }
            }
        }
        // Row to place the TextField and Button horizontally



        // New Button for navigating to the link
        Button(
            onClick = {
                // Open the link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://freevote-60cd6.web.app/"))
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(top = 8.dp) // Add a little spacing from the previous components
                .height(57.dp) // Keep the height the same
                .fillMaxWidth(), // Fill width
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1A911))
        ) {
            Text("Get Testing Details", color = Color.White) // Change text color if needed
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
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(50.dp) // Shadow for Header
    ) {
        Text(
            text = "FREE",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            fontFamily = rubikMoonrocksFont
        )
        Text(
            text = "vote",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontFamily = rubikMoonrocksFont
        )
        Text(
            text = "!",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006400),
            fontFamily = rubikMoonrocksFont
        )
    }
}

// Function to validate the user in Home Affairs
fun validateUserInHomeAffairs(userId: String, callback: (Boolean, String) -> Unit) {
    val homeAffairsRef = firestoreDb.collection("citizens").document(userId)
    homeAffairsRef.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val id = documentSnapshot.getString("id") ?: ""
            callback(true, id) // User is valid, return id
        } else {
            callback(false, "") // User is not valid
        }
    }.addOnFailureListener {
        callback(false, "") // Handle error
    }
}

// Function to validate user in Realtime Database
fun validateUserInRealtimeDb(idNumber: String, callback: (Boolean) -> Unit) {
    val databaseRef = Firebase.database.getReference("users/$idNumber")
    databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            callback(dataSnapshot.exists()) // Check if the user exists
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(false) // Handle error
        }
    })
}

// Function to check if the user is 18 or older based on ID number
fun isUser18OrOlder(idNumber: String): Boolean {
    // Assuming ID number is in the format YYMMDDXXXXXX
    val birthDateString = idNumber.substring(0, 6)
    val birthDate = SimpleDateFormat("yyMMdd", Locale.getDefault()).parse(birthDateString)

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, -18) // Subtract 18 years

    // Compare birth date with the current date minus 18 years
    return birthDate?.let { it.before(calendar.time) } ?: false
}
