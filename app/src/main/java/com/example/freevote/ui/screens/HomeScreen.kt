package com.example.freevote.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen (modifier: Modifier, navController: NavController) {

    Column {
        Text(text = "Welcome")
        Button(onClick = { navController.navigate("pinScreen") }) {

        }
    }




}