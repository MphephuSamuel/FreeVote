package com.example.freevote.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel

@Composable
fun ResultsScreen(navController: NavController, viewModel: MainViewModel) {
    Text(text = "Results Screen")
    Button(onClick = { /*TODO*/ 
        navController.navigate("homenews")}) {
        Text(text = "go home")
    }
}