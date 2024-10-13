package com.example.freevote.ui.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun ConnectivityAlertDialog() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Check if internet is available
    if (!isInternetAvailable(context)) {
        showDialog = true
    }

    // Show alert dialog if not connected
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "No Internet Connection") },
            text = { Text("Please check your internet connection and try again.") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
