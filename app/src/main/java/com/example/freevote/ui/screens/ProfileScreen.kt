package com.example.freevote.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Profile", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Last Name: ${viewModel.lName}", fontSize = 18.sp)
        Text(text = "Names: ${viewModel.names}", fontSize = 18.sp)
        Text(text = "Phone Number: ${viewModel.phoneNumber}", fontSize = 18.sp)
        Text(text = "Email: ${viewModel.email}", fontSize = 18.sp)
        Text(text = "Gender: ${viewModel.gender}", fontSize = 18.sp)
        Text(text = "Address: ${viewModel.address}", fontSize = 18.sp)
    }
}
