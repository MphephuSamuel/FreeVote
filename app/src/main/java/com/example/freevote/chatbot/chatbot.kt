package com.example.freevote.chatbot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freevote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBot(navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }

    // Main chat button
    FloatingActionButton(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .size(40.dp) // Smaller FAB size
            .padding(16.dp),
        containerColor = Color(0xFF6200EE) // Set the color
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chat),
            contentDescription = "Chat",
            modifier = Modifier.size(24.dp) // Smaller icon size
        )
    }

    // Expanded options if isExpanded is true
    if (isExpanded) {
        Column(
            modifier = Modifier
                .wrapContentSize() // Use wrapContentSize for compactness
                .padding(8.dp), // Smaller padding around options
            horizontalAlignment = Alignment.End
        ) {
            // Option 1: Change PIN
            ChatOption("Change PIN") {
                navController.navigate("changePin")
            }
            // Option 2: View Notifications
            ChatOption("View Notifications") {
                navController.navigate("notifications")
            }
            // Option 3: Send Request
            ChatOption("Send Request") {
                navController.navigate("sendRequest")
            }
            // Option 4: View Results
            ChatOption("View Results") {
                navController.navigate("resultsScreen")
            }
        }
    }
}

@Composable
fun ChatOption(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp) // Smaller vertical padding
            .fillMaxWidth(0.8f), // Use a smaller width
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1BEE7)) // Customize your color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Smaller padding for options
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 14.sp) // Smaller font size
            Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null, modifier = Modifier.size(16.dp)) // Smaller icon size
        }
    }
}

