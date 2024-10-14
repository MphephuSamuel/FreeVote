package com.example.freevote.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener





@Composable
fun SettingsScreen(viewModel: MainViewModel, modifier: Modifier, navController: NavController) {
    var currentScreen by remember { mutableStateOf("settings") }

    when (currentScreen) {
        "settings" -> SettingsList(
            onBackClick = { navController.navigate("homenews") },
            onAccountClick = { currentScreen = "account" },
            onHelpClick = { currentScreen = "help" },
            onNotificationClick = { currentScreen = "notifications" }, // Added for notification screen
            navController = navController,
            modifier = modifier
        )
        "account" -> AccountDetails(
            onBackClick = { currentScreen = "settings" },
            modifier = modifier
        )
        "help" -> HelpScreen(
            viewModel = viewModel,
            onBackClick = { currentScreen = "settings" },
            onHelpCenterClick = { currentScreen = "helpCenter" },
            modifier = modifier
        )
        "helpCenter" -> HelpCenterForm(
            viewModel,
            onBackClick = { currentScreen = "help" },
            modifier = modifier
        )
        "notifications" -> NotificationScreen(
            onBackClick = { currentScreen = "settings" },
            modifier = modifier
        ) // Added NotificationScreen
    }
}

@Composable
fun SettingsList(
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onHelpClick: () -> Unit,
    onNotificationClick: () -> Unit, // Added for notification
    navController: NavController,
    modifier: Modifier
) {
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
        SettingItem(icon = Icons.Default.Notifications, title = "Notifications", onClick = onNotificationClick) // Navigate to notifications
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
fun HelpScreen(viewModel: MainViewModel, onBackClick: () -> Unit, onHelpCenterClick: () -> Unit, modifier: Modifier) {
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
fun HelpCenterForm(viewModel: MainViewModel, onBackClick: () -> Unit, modifier: Modifier) {
    val context = LocalContext.current
    var userQuery by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var notificationMessage by remember { mutableStateOf("") }
    val database = FirebaseDatabase.getInstance() // Initialize Realtime Database instance

    val queries = remember { mutableStateListOf<Pair<String, String>>() } // List to store user queries
    val responses = remember { mutableStateListOf<UserResponse>() } // List to store responses from Firebase

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
        idNumber = viewModel.idNumber

        // TextField for user to type their question
        OutlinedTextField(
            value = userQuery,
            onValueChange = { userQuery = it },
            label = { Text("Your Query or Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Button to send data to Realtime Database
        Button(
            onClick = {
                Log.d("HelpCenterForm", "Send button clicked")

                if (idNumber.isNotBlank() && userQuery.isNotBlank()) {
                    val userHelpRequest = hashMapOf(
                        "idNumber" to idNumber,
                        "query" to userQuery
                    )

                    val helpRequestsRef = database.getReference("user_help_requests")
                    helpRequestsRef.push().setValue(userHelpRequest)
                        .addOnSuccessListener {
                            queries.add(Pair(idNumber, userQuery)) // Add query to the list
                            notificationMessage = "Request sent successfully"
                            idNumber = ""
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

        // Display queries and responses
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Previous Queries", fontSize = 23.sp, modifier = Modifier.padding(bottom = 8.dp))

        // Fetch responses from Firebase
        LaunchedEffect(Unit) {
            val responseDatabase: DatabaseReference = database.getReference("users_query_response")
            responseDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    responses.clear()
                    for (data in snapshot.children) {
                        val response = data.getValue(UserResponse::class.java)
                        if (response != null) {
                            responses.add(response)
                        } else {
                            Log.e("HelpCenterForm", "Fetched response is null")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HelpCenterForm", "Error fetching responses: ${error.message}")
                }
            })
        }

        // Display responses
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Responses", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn {
            items(responses) { response ->
                ResponseItem(response)
            }
        }
    }
}

@Composable
fun QueryItem(idNumber: String, query: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFE0F7FA)), // Light cyan background for queries
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ID Number: $idNumber",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = "Query: $query",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ResponseItem(response: UserResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFE8F5E9)), // Light green background for responses
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Query: ${response.query}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = "Response: ${response.response}",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Responded at: ${response.respondedAt}",
                fontSize = 12.sp,
                color = Color(0xFF888888),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


// Data class for UserResponse
data class UserResponse(
    val query: String = "",
    val response: String = "",
    val requestId: String = "",
    val respondedAt: Long = 0L
)

@Composable
fun NotificationScreen(onBackClick: () -> Unit, modifier: Modifier) {
    val notifications = remember { mutableStateListOf<Notification>() }
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("notifications")

    // Fetch notifications from Firebase
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notifications.clear()
                for (data in snapshot.children) {
                    val notification = data.getValue(Notification::class.java)
                    if (notification != null) {
                        notifications.add(notification)
                    } else {
                        Log.e("NotificationScreen", "Fetched notification is null")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NotificationScreen", "Error fetching notifications: ${error.message}")
            }
        })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(text = "Notifications", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Display notifications
        if (notifications.isEmpty()) {
            Text(text = "No notifications available", fontSize = 16.sp)
        } else {
            notifications.forEach { notification ->
                NotificationItem(notification)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle notification click */ }
            .background(Color(0xFFF0F9E8)), // Light green background
        // Elevation for depth
        shape = RoundedCornerShape(12.dp) // Rounded corners for a modern look
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = notification.title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333) // Dark text for better contrast
            )
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color(0xFF555555), // Softer text color for the message
                modifier = Modifier.padding(top = 4.dp)
            )
            }
        }
}

data class Notification(
    val title: String = "",
    val message: String = ""
)

@Composable
fun SettingItem(icon: ImageVector, title: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.padding(end = 16.dp))
        Text(text = title, fontSize =18.sp)
        }
}
