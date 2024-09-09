@file:Suppress("DEPRECATION")

package com.example.myapplication


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.freevote.R
import com.example.freevote.ui.screens.firestoreDb
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore



val rubikMoonrocksFont = FontFamily(
    Font(
        resId = R.font.rubik_moonrocks, // Ensure the font file exists in res/font
        weight = FontWeight.Normal
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdNumberScreen(navController: NavHostController) {

    var idNumber by remember { mutableStateOf("") }
    val context =LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // OutlinedTextField with rounded shape, shadow, and background color
            OutlinedTextField(
                value = idNumber,
                onValueChange = { idNumber = it },
                label = {
                        Text("ID NUMBER:", color = DarkGray)
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    color = White
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(57.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .shadow(8.dp, RoundedCornerShape(0.dp)) // Shadow for TextField
                    .background(Color(0xFFF1A911)),
                shape = RoundedCornerShape(0.dp), // Ensure the shape is applied directly
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Transparent,
                    unfocusedBorderColor = Transparent,
                    cursorColor = White
                )
            )

            Button(
                onClick = { /* Handle click */
                    validateUserInHomeAffairs(idNumber) { isValid, id ->
                        if (isValid) {
                            // ID is valid, proceed to registration
                            navController.navigate("registrationScreen/$id")
                        } else {
                            // ID is invalid, show an error message
                            Toast.makeText(context, "Invalid ID number", Toast.LENGTH_SHORT).show()
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
            } else {callback(false, userId)
            }
        }
        .addOnFailureListener { e ->
            println("Error validating user in Firestore: $e")
            callback(false, userId)
        }
}





