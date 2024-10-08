package com.example.freevote.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinScreen(navController: NavController,
              idNumber: String,
              viewModel: MainViewModel, // Make sure to use the correct ViewModel type
              modifier: Modifier = Modifier // Default value for modifier
)
{
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    // Instead of using 'remember', we use the ViewModel to store the PIN
    var pin by remember { mutableStateOf(TextFieldValue(viewModel.pinCode ?: "")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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

        Row {
            // PIN input field with yellow background
            TextField(
                value = pin,
                onValueChange = { newValue ->
                    // Filter out non-numeric characters and limit to 5 digits
                    if (newValue.text.all { it.isDigit() } && newValue.text.length <= 5) {
                        pin = newValue
                        // Update ViewModel with the new PIN
                        viewModel.updatePinCode(newValue.text)
                    }
                },
                label = { Text("PIN") },
                modifier = Modifier
                    .height(57.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFF1A911)),
                shape = RoundedCornerShape(0.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    // Navigate to homeScreen
                    navController.navigate("homenews")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1A911))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
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
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Voting Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .size(500.dp)
        )
    }
}