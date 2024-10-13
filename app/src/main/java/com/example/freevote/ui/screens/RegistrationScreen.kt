
package com.example.freevote.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel


val RubikMoonrocks = FontFamily(
    Font(R.font.rubik_moonrocks)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel, idNumber: String ) {
    // Access values directly from ViewModel

    var lName = viewModel.lName
    var names = viewModel.names
    var phoneNumber = viewModel.phoneNumber
    var email = viewModel.email
    var gender = viewModel.gender
    var address = viewModel.address
    var scrollState = rememberScrollState()
    var context = LocalContext.current

    Spacer(modifier = Modifier.height(30.dp))
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

        // Registration card
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REGISTRATION",
                    color = Color.Red,
                    fontSize = 33.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                // Input fields using ViewModel state
                TextField(
                    value = lName,
                    onValueChange = { viewModel.updateLastName(it) },
                    label = { Text("Last Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    ),
                )

                TextField(
                    value = names,
                    onValueChange = { viewModel.updateNames(it) },
                    label = { Text("Names") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    )
                )

                TextField(
                    value = phoneNumber,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 10) {
                            viewModel.updatePhoneNumber(newValue)
                        }
                    },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    )
                )

                TextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it.trim()) },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    )
                )

                TextField(
                    value = gender,
                    onValueChange = { viewModel.updateGender(it.trim()) },
                    label = { Text("Gender") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    )
                )

                TextField(
                    value = address,
                    onValueChange = { viewModel.updateAddress(it.trim()) },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .border(1.dp, Color.Black, RectangleShape),
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF1A911),
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons for navigating
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { navController.navigate("idNumberScreen") },
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
                        onClick = onClick@{
                            if (idNumber.isBlank()) {
                                Toast.makeText(context, "ID Number is required", Toast.LENGTH_SHORT).show()
                                return@onClick
                            }
                            lName=viewModel.lName.trim()
                            names=viewModel.names.trim()
                            phoneNumber=viewModel.phoneNumber.trim()
                            email=viewModel.email.trim()
                            gender=viewModel.gender.trim()
                            address=viewModel.address.trim()

                            validateUserDetailsInHomeAffairs(idNumber, lName, names, gender) { isValid, userId, lastName, names, gender ->
                                if (isValid) {
                                    navController.navigate("createPinScreen/$userId/$lastName/$names/${viewModel.phoneNumber}/${viewModel.email}/${viewModel.gender}/${viewModel.address}")
                                } else {
                                    Toast.makeText(context, "Invalid details", Toast.LENGTH_SHORT).show()
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
}


// Function to validate user in Firestore (Home Affairs)

fun validateUserDetailsInHomeAffairs(userId: String, lastName : String, names : String, gender : String, callback : (Boolean,String,String, String, String) -> Unit) {
    firestoreDb.collection("citizens").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val storedLastName = document.getString("lname")
                val storedNames = document.getString("names")
                val storedGender = document.getString("gender")
                println(storedLastName==lastName)
                println(names==storedNames)
                println(gender==storedGender)
                if (storedLastName == lastName && storedNames == names && storedGender == gender) {
                    println("User validated successfully in Home Affairs DB")
                    callback(true, userId, lastName, names, gender)
                    // User is valid, allow voting or other actions
                } else {
                    println("Validation failed: Details Mismatch at home Affairs DB")
                    callback(false, userId, lastName, names, gender)
                    // Name does not match, show an error message
                }
            }
        }
}
