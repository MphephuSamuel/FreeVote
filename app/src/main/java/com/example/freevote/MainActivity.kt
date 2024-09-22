package com.example.freevote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freevote.ui.screens.CreatePinScreen
import com.example.freevote.ui.screens.HomeScreen
import com.example.freevote.ui.screens.PinScreen
import com.example.freevote.ui.screens.RegistrationScreen
import com.example.myapplication.IdNumberScreen
import com.example.myproject1.ui.theme.FreeVoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreeVoteTheme {
                Navigation()

            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "idNumberScreen") {
        composable("idNumberScreen") { IdNumberScreen(navController) }
        composable("homeScreen"){ HomeScreen(Modifier, navController)}
        composable(
            route = "registrationScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            RegistrationScreen(
                navController = navController,
                idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            )
        }
        composable(
            route = "pinScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            PinScreen(
                navController = navController,
                idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: "",
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
                email = backStackEntry.arguments?.getString("EMAIL") ?: "",
                gender = backStackEntry.arguments?.getString("GENDER") ?: "",
                address = backStackEntry.arguments?.getString("ADDRESS") ?: ""
            )
        }
    }
}