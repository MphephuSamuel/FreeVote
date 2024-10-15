package com.example.freevote.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.util.PreferencesUtil
import kotlinx.coroutines.delay

val rubikMoonrocksFont = FontFamily(Font(R.font.rubik_moonrocks))

@Composable
fun TermsAndConditionsScreen(navController: NavController, onAccept: () -> Unit) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()) // Adjust for system bars
            .padding(horizontal = 16.dp) // Additional horizontal padding
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Text(
            text = "Terms and Conditions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Conditions text
        Text(
            text = """
                TERMS AND CONDITIONS OF THE VOTING APPLICATION

                Welcome to the FREEvote! Application. By accessing and using this application, you signify that you have read and agree to comply with and be bound by these Terms and Conditions of Use. If you do not agree to these terms, you must exit the application immediately.

                INTERPRETATION

                The terms "FREEvote!", "we", "us", or "our" refer to the voting application.
                The term "you" refers to the user of the voting application.

                GENERAL DISCLAIMER

                The FREEvote! application is provided for informational purposes only and aims to facilitate voting processes in a secure and accessible manner. We make no representations or warranties regarding the accuracy, completeness, or reliability of the information provided within this app. The use of this application is entirely at your own risk, and we are not liable for any damages arising from such use.

                INTELLECTUAL PROPERTY RIGHTS

                Unless otherwise indicated, all intellectual property rights, including text, design, graphics, and logos related to FREEvote! are owned by us. You may not reproduce or use any of these materials without our prior written consent.

                TERMS OF USE

                - You are granted a non-exclusive, non-transferable, revocable license to use this application strictly for personal, non-commercial purposes.
                - You may not attempt to modify, decompile, or reverse-engineer the application code.

                GUIDELINES FOR USER CONDUCT

                - You agree to use the application only for lawful purposes and in accordance with these Terms.
                - You agree not to interfere with or disrupt the application, including transmitting any viruses or harmful code.
                - Any misuse of the application, including tampering with voting data or attempting to compromise the security of the system, will result in immediate termination of access and may lead to legal consequences.

                PRIVACY STATEMENT

                We are committed to protecting your privacy. Any personal information collected during the use of this application will be handled in accordance with applicable privacy laws and will not be shared without your consent.

                MODIFICATIONS TO TERMS

                FREEvote! reserves the right to modify these terms at any time. You will be notified of any changes, and by continuing to use the application, you agree to comply with and be bound by the updated terms.

                CONTACT

                For any questions or issues regarding these Terms and Conditions, please contact us through the support section of the application.
            """.trimIndent(),
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                onClick = {
                    // Save the acceptance to shared preferences
                    onAccept() // Call the callback to save acceptance and navigate
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7609))
            ) {
                Text(text = "Accept", color = Color.White)
            }
            // Add a variable to track clicks
            var declineClickCount by remember { mutableStateOf(0) }

            Button(
                onClick = {
                    declineClickCount++ // Increment the click count

                    // Check the number of clicks
                    if (declineClickCount == 1) {
                        Toast.makeText(
                            context,
                            "Tap again to leave the app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (declineClickCount == 2) {
                        // Close the current activity and leave the app
                        (context as? Activity)?.finishAffinity()
                    }

                    // Reset the counter after 2 seconds

                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Decline", color = Color.White)
            }


        }
    }
}

@Composable
fun TermsAndConditionsScreenReadOnly() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background to white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Terms and Conditions",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = """
                    TERMS AND CONDITIONS OF THE VOTING APPLICATION
    
                    Welcome to the FREEvote! Application. By accessing and using this application, you signify that you have read and agree to comply with and be bound by these Terms and Conditions of Use. If you do not agree to these terms, you must exit the application immediately.
    
                    INTERPRETATION
    
                    The terms "FREEvote!", "we", "us", or "our" refer to the voting application.
                    The term "you" refers to the user of the voting application.
    
                    GENERAL DISCLAIMER
    
                    The FREEvote! application is provided for informational purposes only and aims to facilitate voting processes in a secure and accessible manner. We make no representations or warranties regarding the accuracy, completeness, or reliability of the information provided within this app. The use of this application is entirely at your own risk, and we are not liable for any damages arising from such use.
    
                    INTELLECTUAL PROPERTY RIGHTS
    
                    Unless otherwise indicated, all intellectual property rights, including text, design, graphics, and logos related to FREEvote! are owned by us. You may not reproduce or use any of these materials without our prior written consent.
    
                    TERMS OF USE
    
                    - You are granted a non-exclusive, non-transferable, revocable license to use this application strictly for personal, non-commercial purposes.
                    - You may not attempt to modify, decompile, or reverse-engineer the application code.
    
                    GUIDELINES FOR USER CONDUCT
    
                    - You agree to use the application only for lawful purposes and in accordance with these Terms.
                    - You agree not to interfere with or disrupt the application, including transmitting any viruses or harmful code.
                    - Any misuse of the application, including tampering with voting data or attempting to compromise the security of the system, will result in immediate termination of access and may lead to legal consequences.
    
                    PRIVACY STATEMENT
    
                    We are committed to protecting your privacy. Any personal information collected during the use of this application will be handled in accordance with applicable privacy laws and will not be shared without your consent.
    
                    MODIFICATIONS TO TERMS
    
                    FREEvote! reserves the right to modify these terms at any time. You will be notified of any changes, and by continuing to use the application, you agree to comply with and be bound by the updated terms.
    
                    CONTACT
    
                    For any questions or issues regarding these Terms and Conditions, please contact us through the support section of the application.
                """.trimIndent(),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

