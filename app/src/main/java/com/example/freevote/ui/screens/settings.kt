package com.example.freevote.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    firestore: FirebaseFirestore  // Firestore is required for AccountDetails
) {
    var currentScreen by remember { mutableStateOf("settings") }

    when (currentScreen) {
        "settings" -> SettingsList(
            onBackClick = { navController.navigate("homenews") },
            onAccountClick = { currentScreen = "account" },
            onHelpClick = { currentScreen = "help" },
            onPrivacyClick = { currentScreen = "privacy" },
            onNotificationClick = { currentScreen = "notifications" },
            onAboutClick = { currentScreen = "about" },  // Added for navigating to AboutScreen
            navController = navController,
            modifier = modifier
        )
        "account" -> AccountDetails(
            onBackClick = { currentScreen = "settings" },
            modifier = modifier,
            firestore = firestore,
            navController = navController
        )
        "help" -> HelpScreen(
            viewModel = viewModel,
            onBackClick = { currentScreen = "settings" },
            onHelpCenterClick = { currentScreen = "helpCenter" },
            modifier = modifier
        )
        "helpCenter" -> HelpCenterForm(
            viewModel = viewModel,
            onBackClick = { currentScreen = "help" },
            modifier = modifier
        )
        "notifications" -> NotificationScreen(
            onBackClick = { currentScreen = "settings" },
            modifier = modifier
        )
        "about" -> AboutScreen(
            paddingValues = PaddingValues(),  // Call AboutScreen when "About" is clicked
        )
    }
}


@Composable
fun SettingsList(
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onPrivacyClick: () -> Unit,
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
        SettingItem(icon = Icons.Default.Notifications, title = "Notifications", onClick = onNotificationClick) // Navigate to notifications
        SettingItem(icon = Icons.Default.Info, title = "Help", onClick = onHelpClick)
        SettingItem(icon = Icons.Default.Lock, title = "About", onClick = onAboutClick)
    }
}

@Composable
fun AccountDetails(
    onBackClick: () -> Unit,
    navController: NavController, // Add NavController here
    modifier: Modifier,
    firestore: FirebaseFirestore
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

        Text(text = "Account Settings", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Delete Account Setting Item
        SettingItem(icon = Icons.Default.Delete, title = "Delete Account", onClick = {
            // Navigate to DeleteAccount screen
            val idNumber = "user-id-123" // Replace with actual logic to get the user's ID
            navController.navigate("DeleteAccount/$idNumber")
        })

        // Change PIN Setting Item
        SettingItem(icon = Icons.Default.Edit, title = "Change PIN", onClick = {
            // Navigate to Change PIN Screen
            navController.navigate("changePin")
        })
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
fun formatRespondedAt(respondedAt: Long): String {
    // Create a SimpleDateFormat object for the desired format
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    // Convert the Long timestamp to a Date object
    val date = Date(respondedAt)

    // Format the Date object to a string
    return dateFormat.format(date)
}


@Composable
fun ResponseItem(response: UserResponse) {
    val formattedDate = formatRespondedAt(response.respondedAt)
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
                text = "Responded at: $formattedDate",
                fontSize = 12.sp,
                color = Color(0xFF888888),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


@Composable
fun AboutScreen(paddingValues: PaddingValues) {
    // Content for About
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background to white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Enable vertical scrolling
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "About the Free Vote App",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "The Free Vote App is an innovative and user-friendly mobile application designed to revolutionize the voting process in South Africa. Developed by the BlackBulls group, the app is tailored specifically to facilitate and streamline national compensatory, national regional, and provincial legislature voting. Our mission is to empower every South African voter by providing a secure, accessible, and transparent platform that ensures their voices are heard during elections.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Our Purpose",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "In South Africa’s democratic landscape, voting is the cornerstone of civic participation. The Free Vote App is designed to simplify the complex electoral process, ensuring that voters can participate seamlessly in all types of elections, from national compensatory votes to regional and provincial legislature elections. This app provides an efficient digital solution, making it easier for citizens to cast their votes securely and confidently.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Key Features",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "- User-Centric Design: Built with a focus on usability, ensuring voters can navigate the platform with ease.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "- Comprehensive Voting Options: Users can participate in national compensatory, national regional, and provincial legislature elections.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "- Secure Voting: Using advanced encryption, the app ensures the integrity and security of the voting process.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "- Accessibility and Inclusivity: The app is inclusive, providing access to voters in urban, rural, and underserved communities.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Designed by the BlackBulls Group",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "The BlackBulls group, a team of dedicated South African developers, is passionate about using technology to improve societal systems. Our goal is to simplify the voting process and promote transparency, accuracy, and accessibility in elections.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Our Vision",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Our vision is to empower South Africans through technology, ensuring that voting is simple, secure, and accessible for everyone. We aim to encourage higher voter turnout and ensure that every South African can have a voice in shaping the country’s future.",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
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
        // Back button
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        // Title and clear all button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Notifications", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

            // Button to clear all notifications
            TextButton(onClick = {
                // Clear all notifications from Firebase
                database.removeValue().addOnSuccessListener {
                    notifications.clear() // Clear locally after successful deletion
                }.addOnFailureListener {
                    Log.e("NotificationScreen", "Error clearing notifications: ${it.message}")
                }
            }) {
                Text(text = "Clear All", color = Color.Red)
            }
        }

        // Display notifications
        if (notifications.isEmpty()) {
            Text(text = "No notifications available", fontSize = 16.sp)
        } else {
            notifications.forEach { notification ->
                NotificationItem(notification, onDeleteClick = { notificationId ->
                    // Delete specific notification from Firebase
                    database.child(notificationId).removeValue().addOnSuccessListener {
                        notifications.removeIf { it.id == notificationId } // Remove locally
                    }.addOnFailureListener {
                        Log.e("NotificationScreen", "Error deleting notification: ${it.message}")
                    }
                })
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onDeleteClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle notification click */ }
            .background(Color(0xFFF0F9E8)), // Light green background
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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

                // Delete button for the specific notification
                IconButton(onClick = { onDeleteClick(notification.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

data class Notification(
    val id: String ="",
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
