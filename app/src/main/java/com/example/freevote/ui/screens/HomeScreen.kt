package com.example.freevote.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel

@Composable
fun HomeScreen (modifier: Modifier, navController: NavController, viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    Column(modifier=Modifier.verticalScroll(scrollState)) {
        Text(text = "Welcome")
        Button(onClick = { navController.navigate("pinScreen") }) {

        }
    }




}