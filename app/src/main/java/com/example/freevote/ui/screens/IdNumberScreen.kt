@file:Suppress("DEPRECATION")

package com.example.myapplication


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.example.freevote.viewmodel.MainViewModel
import com.example.freevote.R
import com.example.freevote.ui.screens.firestoreDb
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


val rubikMoonrocksFont = FontFamily(
    Font(
        resId = R.font.rubik_moonrocks, // Ensure the font file exists in res/font
        weight = FontWeight.Normal
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdNumberScreen(navController: NavHostController, viewModel: MainViewModel) {

    var idNumber by remember { mutableStateOf(viewModel.idNumber ?: "") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                    color = Color.Black,
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

        // Row to place the TextField and Button horizontally
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
                        idNumber = newId
                        viewModel.updateIdNumber(newId)
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
                    )
                )

                Button(
                    onClick = {
                        if (idNumber.isBlank()) {
                            Toast.makeText(context, "Please enter your ID number", Toast.LENGTH_SHORT).show()
                        } else {
                            validateUserInHomeAffairs(idNumber) { isValidFirestore, id ->
                                if (isValidFirestore) {
                                    // ID is valid in Firestore, now check Realtime Database
                                    validateUserInRealtimeDb(idNumber) { isValidRealtime ->
                                        if (isValidRealtime) {
                                            // ID is valid in both, proceed to pin screen
                                            navController.navigate("pinScreen/$id")
                                        } else {
                                            // ID is only valid in Firestore, update the ViewModel and proceed to registration screen
                                            viewModel.updateIdNumber(idNumber) // Update ViewModel here
                                            navController.navigate("registrationScreen/$idNumber")
                                        }
                                    }
                                } else {
                                    // ID is invalid in Firestore, show an error message
                                    Toast.makeText(context, "Invalid ID number", Toast.LENGTH_SHORT).show()
                                }
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
        Spacer(modifier = Modifier.height(130.dp))
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

fun validateUserInHomeAffairs(userId: String,callback: (Boolean, String) -> Unit) {
    firestoreDb.collection("citizens").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                callback(true, userId)
            } else {
                callback(false, userId)
            }
        }
        .addOnFailureListener { e ->
            println("Error validating user in Firestore: $e")
            callback(false, userId)
        }
}

fun validateUserInRealtimeDb(userId: String, callback: (Boolean) -> Unit) {
    val usersRef = Firebase.database.reference.child("users").child(userId)

    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                callback(true)
            } else {
                callback(false)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            println("Error validating user in Realtime Database: ${error.toException()}")
            callback(false)
        }
    })
}