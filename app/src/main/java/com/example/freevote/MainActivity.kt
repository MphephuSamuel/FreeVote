package com.example.freevote

import WebViewScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freevote.ui.screens.*
import com.example.myproject1.ui.theme.FreeVoteTheme
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // Use activity-scoped ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreeVoteTheme {
                val hasAcceptedTerms = checkIfTermsAccepted() // Check if user accepted the terms
                Navigation(viewModel, hasAcceptedTerms)
            }
        }
    }

    // Check if the terms have been accepted using SharedPreferences
    private fun checkIfTermsAccepted(): Boolean {
        val sharedPreferences = getSharedPreferences("freevote_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("terms_accepted", false)
    }

    // Save the terms acceptance state in SharedPreferences
    fun saveTermsAccepted() {
        val sharedPreferences = getSharedPreferences("freevote_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("terms_accepted", true)
        editor.apply()
    }
}

@Composable
fun Navigation(viewModel: MainViewModel, hasAcceptedTerms: Boolean) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (hasAcceptedTerms) "idNumberScreen" else "terms"
    ) {
        composable("idNumberScreen") { IdNumberScreen(navController, viewModel) }
        composable("homeScreen") { HomeScreen(Modifier, navController, viewModel) }
        composable("homenews") { MainScreen(navController, viewModel) }
        composable("vote") { VotePage(Modifier, navController, viewModel) }
        composable("Calender") { Calender() }
        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                firestore = FirebaseFirestore.getInstance()  // Pass Firestore instance
            )
        }

        composable("changePin") { ChangePinScreen(Modifier, navController, viewModel) }
        composable("profile") { ProfileScreen(viewModel, navController) }
        composable("webViewScreen/{url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(url = url)
        }

        composable(
            route = "forgotPin/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            ForgotPinScreen(
                modifier = Modifier,
                navController = navController,
                idNumber = idNumber,
                viewModel = viewModel // Pass ViewModel as the last argument

            )
        }

        composable("terms") {
            TermsAndConditionsScreen(
                navController = navController,
                onAccept = {
                    // Save acceptance and navigate
                    (navController.context as MainActivity).saveTermsAccepted()
                    navController.navigate("idNumberScreen")
                }
            )
        }

        composable(
            route = "registrationScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            RegistrationScreen(
                navController = navController,
                idNumber = idNumber,
                viewModel = viewModel // Pass ViewModel as the last argument
            )
        }

        composable(
            route = "pinScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            PinScreen(
                navController = navController,
                idNumber = idNumber,
                viewModel = viewModel, // Pass ViewModel as the last argument
                modifier = Modifier
            )
        }


        composable(
            route = "CreatePinScreen/{ID_NUMBER}/{LAST_NAME}/{NAMES}/{PHONE_NUMBER}/{EMAIL}/{GENDER}/{ADDRESS}",
            arguments = listOf(
                navArgument("ID_NUMBER") { type = NavType.StringType },
                navArgument("LAST_NAME") { type = NavType.StringType },
                navArgument("NAMES") { type = NavType.StringType },
                navArgument("PHONE_NUMBER") { type = NavType.StringType },
                navArgument("EMAIL") { type = NavType.StringType },
                navArgument("GENDER") { type = NavType.StringType },
                navArgument("ADDRESS") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CreatePinScreen(
                navController = navController,
                idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: "",
                lastName = backStackEntry.arguments?.getString("LAST_NAME") ?: "",
                names = backStackEntry.arguments?.getString("NAMES") ?: "",
                phoneNumber = backStackEntry.arguments?.getString("PHONE_NUMBER") ?: "",
                theEmail = backStackEntry.arguments?.getString("EMAIL") ?: "",
                gender = backStackEntry.arguments?.getString("GENDER") ?: "",
                address = backStackEntry.arguments?.getString("ADDRESS") ?: "",
                viewModel = viewModel // Pass ViewModel as the last argument
            )
        }
        composable(
            route = "DeleteAccount/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            DeleteAccount(

                navController = navController,
                viewModel
            )

        }
        // Account Details Screen Route
        composable("accountDetails") {
            AccountDetails(
                onBackClick = { navController.popBackStack() },
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                firestore = FirebaseFirestore.getInstance()
            )
        }

        // Change PIN Screen Route
        composable("changePin") {
            ChangePinScreen(
                modifier = Modifier,
                navController,
                viewModel // Pass the appropriate ViewModel
            )
        }
        composable(
            route = "DeleteAccount/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            DeleteAccount(
                navController = navController,
                viewModel
            )
        }
        composable("webViewScreen/{url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(url)
        }
        composable("PrivacyScreen") { PrivacyScreen(onBackClick = { navController.popBackStack() }, modifier = Modifier.fillMaxSize()) }
        composable("termsAndConditionsReadOnly") { TermsAndConditionsScreenReadOnly() }
    }
}