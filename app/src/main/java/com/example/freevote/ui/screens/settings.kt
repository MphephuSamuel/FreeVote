package com.example.freevote.ui.screens


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SettingsScreen(modifier: Modifier, navController: NavController) {
    // State variable to track which screen is displayed
    var currentScreen by remember { mutableStateOf("settings") }

    when (currentScreen) {
        "settings" -> SettingsList(
            onBackClick = { navController.navigate("homenews") },
            onAccountClick = { currentScreen = "account" },
            onHelpClick = { currentScreen = "help" },
            modifier = modifier
        )
        "account" -> AccountDetails(
            onBackClick = { currentScreen = "settings" },
            modifier = modifier
        )
        "help" -> HelpScreen(
            onBackClick = { currentScreen = "settings" },
            onHelpCenterClick = { currentScreen = "helpCenter" },
            modifier = modifier
        )
        "helpCenter" -> HelpCenterForm(
            onBackClick = { currentScreen = "help" },
            modifier = modifier
        )
    }
}

@Composable
fun SettingsList(onBackClick: () -> Unit,onAccountClick: () -> Unit, onHelpClick: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(text = "Settings", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        // Settings options
        SettingItem(icon = Icons.Default.AccountCircle, title = "Account", onClick = onAccountClick)
        SettingItem(icon = Icons.Default.Lock, title = "Privacy")
        SettingItem(icon = Icons.Default.Person, title = "Avatar")
        SettingItem(icon = Icons.Default.Notifications, title = "Notifications")
        SettingItem(icon = Icons.Default.Settings, title = "Storage and Data")
        SettingItem(icon = Icons.Default.Info, title = "Help", onClick = onHelpClick)
    }
}

@Composable
fun AccountDetails(onBackClick: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(text = "Account Settings", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Account details options
        SettingItem(icon = Icons.Default.Notifications, title = "Security Notifications")
        SettingItem(icon = Icons.Default.Info, title = "Request Account Info")
        SettingItem(icon = Icons.Default.Add, title = "Add Account")
        SettingItem(icon = Icons.Default.Delete, title = "Delete Account")
    }
}

@Composable
fun HelpScreen(onBackClick: () -> Unit, onHelpCenterClick: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(text = "Help", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Help details options
        SettingItem(icon = Icons.Default.Info, title = "Help Center", onClick = onHelpCenterClick)
        SettingItem(icon = Icons.Default.Info, title = "App Information")
    }
}

@Composable
fun HelpCenterForm(onBackClick: () -> Unit, modifier: Modifier) {
    val context = LocalContext.current
    var userQuery by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var notificationMessage by remember { mutableStateOf("") }
    val firestore = FirebaseFirestore.getInstance() // Initialize Firestore instance

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(text = "Help Center", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // TextField for username input
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Your Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // TextField for user to type their question
        OutlinedTextField(
            value = userQuery,
            onValueChange = { userQuery = it },
            label = { Text("Your Query or Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Button to send data to Firestore
        Button(
            onClick = {
                Log.d("HelpCenterForm", "Send button clicked")

                if (username.isNotBlank() && userQuery.isNotBlank()) {
                    val userHelpRequest = hashMapOf(
                        "username" to username,
                        "query" to userQuery
                    )

                    firestore.collection("user_help_requests")
                        .add(userHelpRequest)
                        .addOnSuccessListener {
                            notificationMessage = "Request sent successfully"
                            username = ""
                            userQuery = "" // Clear the form
                        }
                        .addOnFailureListener { e ->
                            notificationMessage = "Failed to send request: ${e.message}"
                            Log.e("HelpCenterForm", "Error adding document", e) // Log the error
                        }
                } else {
                    notificationMessage = "Please fill in both fields."
                    Log.d("HelpCenterForm", "Username or query is empty")
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send")
        }


        // Display notification message
        if (notificationMessage.isNotEmpty()) {
            Text(
                text = notificationMessage,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Composable
fun SettingItem(icon: ImageVector, title: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 18.sp)
    }
}