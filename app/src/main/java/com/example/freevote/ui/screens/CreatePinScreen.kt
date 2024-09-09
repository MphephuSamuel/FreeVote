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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePinScreen(modifier: Modifier = Modifier, navController: NavController) {
    val pinChange = remember { mutableStateOf(TextFieldValue()) }
    val confirm = remember { mutableStateOf(TextFieldValue()) }

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
                        // Filter out non-numeric characters
                        if (newValue.text.all { it.isDigit() } && newValue.text.length <= 5) {
                            pinChange.value = newValue
                        }
                    },
                    label = { Text("Change Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = confirm.value,
                    onValueChange = { newValue ->
                        // Filter out non-numeric characters
                        if (newValue.text.all { it.isDigit() } && newValue.text.length <= 5) {
                            confirm.value = newValue
                        }
                    },
                    label = { Text("Confirm Pin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                    shape = RoundedCornerShape(0.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
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
                            navController.navigate("homeScreen")
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
}
