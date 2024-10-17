package com.example.freevote.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.freevote.ui.screens.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

// MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    val navController1 = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Track back press state
    var backPressedOnce by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
// Handle back button press
    BackHandler {
        if (backPressedOnce) {
            // Reset ViewModel variables
            viewModel.resetAllVariables()

            // Exit the app
            (context as Activity).finish()
        } else {
            // Show a message on first back press
            backPressedOnce = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()

            // Reset backPressedOnce after 2 seconds
            coroutineScope.launch {
                delay(2000L)
                backPressedOnce = false
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier,
                drawerContainerColor = Color.White
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // Drawer menu items with click actions
                Button(
                    onClick = { navController.navigate("profile") },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Person Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp) // Match the size to the font size (18.sp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            viewModel.lName + " " + viewModel.names,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /*TODO*/
                    navController.navigate("settings")
                              },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Settings",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        navController.navigate("termsAndConditionsReadOnly") // Navigate to the simplified read-only screen
                    },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Terms & Conditions",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        // Navigate to the PrivacyScreen
                        navController.navigate("PrivacyScreen") // Replace "privacy_screen" with the actual route name for the PrivacyScreen
                    },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Privacy Policy",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        // Sign out the user from Firebase
                        FirebaseAuth.getInstance().signOut()
                        viewModel.resetAllVariables()

                        // Navigate to the idNumberScreen
                        navController.navigate("idNumberScreen") {

                            // Optionally clear the back stack
                            popUpTo("idNumberScreen") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(0.dp), // To adjust corner radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth() // Full width for the button
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Align text to the start (left)
                    ) {
                        Text(
                            "Sign Out",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth() // Ensures the text takes up full width
                        )
                    }
                }

            }
        },
        drawerState = drawerState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically, // Align items vertically centered in the Row
                            horizontalArrangement = Arrangement.SpaceBetween // Ensures title and timer are spaced apart
                        ) {
                            // Title "FREEvote!"
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = Color.Black)) {
                                        append("FREE")
                                    }
                                    withStyle(style = SpanStyle(color = Color.Red)) {
                                        append("vote")
                                    }
                                    withStyle(style = SpanStyle(color = Color(0xFF006400))) {
                                        append("!")
                                    }
                                },
                                fontFamily = rubikMoonrocksFont,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 48.sp,
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(4f, 4f),
                                        blurRadius = 8f
                                    )
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = { BottomNavigationBar(navController1 = navController1, navController = navController) },
            content = { paddingValues ->
                NavigationContent(navController1 = navController1, paddingValues = paddingValues)
            }
        )


    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu Item 1", modifier = Modifier.padding(8.dp))
        Text(text = "Menu Item 2", modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun NavigationContent(navController1: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController1, startDestination = "home") {
        composable("home") { HomeScreen(paddingValues)  }
        composable("vote") { VoteScreen(paddingValues) }
        composable("results") { ResultsScreen(paddingValues) }
        composable("Calender") { Calender() }
    }
}

@Composable
fun BottomNavigationBar(navController1: NavHostController, navController: NavController) {
    val context = LocalContext.current
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController1.navigate("home"){
                popUpTo("home")// Clear back stack
                launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.vote_icon), // Replace with your drawable resource
                contentDescription = "Vote",
                modifier = Modifier.size(25.dp)
            ) },
            label = { Text("Vote") },
            selected = false,
            onClick = {
                val userId = FirebaseAuth.getInstance().currentUser?.uid // Get the current user's ID
                if (userId != null) {
                    val voteStatusRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("votes/$userId")

                    // Check if the user has already voted
                    voteStatusRef.child("hasVoted").get().addOnSuccessListener { dataSnapshot ->
                        val hasVoted = dataSnapshot.getValue(Boolean::class.java) ?: false

                        if (!hasVoted) {
                            // User has not voted, proceed to check if voting is allowed
                            val votingManagementRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("voting_management")

                            votingManagementRef.child("isVotingAllowed").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val isVotingAllowed = snapshot.getValue(Boolean::class.java) ?: false // Default to false if null
                                    if (isVotingAllowed) {
                                        navController.navigate("vote") // Navigate if voting is allowed
                                    } else {
                                        Toast.makeText(context, "No voting allowed", Toast.LENGTH_SHORT).show() // Show toast if not allowed
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error checking voting status", Toast.LENGTH_SHORT).show() // Handle error case
                                }
                            })
                        } else {
                            // User has already voted, show a message
                            Toast.makeText(context, "You have already voted.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error checking voting status", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // User is not authenticated
                    Toast.makeText(context, "You must be logged in to vote.", Toast.LENGTH_SHORT).show()
                }
            }

        )
        NavigationBarItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.result_icon), // Replace with your drawable resource
                contentDescription = "Vote",
                modifier = Modifier.size(25.dp)
            ) },
            label = { Text("Results") },
            selected = false,
            onClick = { navController1.navigate("results"){
                popUpTo("home")// Clear back stack
                launchSingleTop = true
            } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Calendar") }
            ,
            label = { Text("Calender") },
            selected = false,
            onClick = { navController1.navigate("Calender"){
                popUpTo("home")// Clear back stack
                launchSingleTop = true
            } }

        )
    }
}

@Composable
fun PrivacyScreen(onBackClick: () -> Unit, modifier: Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            item {
                Text(text = "Privacy Policy", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

                Text(
                    text = "At My Voting App, we are committed to protecting your privacy. We collect and store your data securely in accordance with the latest data protection standards. Your information is never shared with third parties without your consent. For more information, please refer to our full privacy policy.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "Key Highlights:",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(text = "- Data collection is minimal and used only to improve your experience.")
                Text(text = "- Your voting data is encrypted and stored securely.")
                Text(text = "- You have the right to access, modify, or delete your data.")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Contact Us", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "If you have any questions about our privacy practices or need assistance with your data, please reach out to us using the following contact details:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "Email: freevote923@gmail.com",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:freevote923@gmail.com")
                        }
                        context.startActivity(intent)
                    }
                )

                Text(
                    text = "Phone: 063 258 9965",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:0632589965")
                        }
                        context.startActivity(intent)
                    }
                )

                Text(text = "Address: 123 Building 13 University of Mpumalanga, Mbombela, South Africa", fontSize = 16.sp)
            }
        }
    }
}
// Function to start the countdown
fun countdown(
    initialTime: Long,
    onTimeChange: (Long) -> Unit,
    onVotingEnd: () -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Main)
    scope.launch {
        var remainingTime = initialTime
        while (remainingTime > 0) {
            delay(1000)
            remainingTime -= 1000
            onTimeChange(remainingTime)
        }
        onVotingEnd()
    }
}

// Updated TimerScreen to receive a modifier argument
@Composable
fun TimerScreen(modifier: Modifier = Modifier) {
    var votingEndTime by remember { mutableStateOf(0L) }
    var timeLeft by remember { mutableStateOf(0L) }
    var isVotingAllowed by remember { mutableStateOf(false) }
    var isVotingActive by remember { mutableStateOf(false) }

    // Listen for changes in Firebase data
    FirebaseVotingEndTime { endTime, votingAllowed ->
        votingEndTime = endTime
        timeLeft = votingEndTime - System.currentTimeMillis()
        isVotingAllowed = votingAllowed

        // If voting is allowed and the end time is in the future, start the countdown
        if (isVotingAllowed && timeLeft > 0) {
            countdown(
                initialTime = timeLeft,
                onTimeChange = { updatedTime ->
                    timeLeft = updatedTime
                    Log.d("Timer", "Time left: $timeLeft ms")
                },
                onVotingEnd = {
                    isVotingActive = false
                    Log.d("Timer", "Voting ended.")
                }
            )
            isVotingActive = true
        } else {
            isVotingActive = false
            Log.d("Timer", "Voting is not active or countdown expired.")
        }
    }

    // Only show the countdown if voting is allowed and active
    if (isVotingAllowed && isVotingActive) {
        CountdownTimer(timeLeft, modifier)  // Display the countdown timer
    } else {
        Log.d("Timer", "Voting is either not allowed or inactive.")
    }
}


// CountdownTimer function now applies the modifier received
@Composable
fun CountdownTimer(timeLeft: Long, modifier: Modifier = Modifier) {
    val hours = (timeLeft / 1000 / 60 / 60) % 24
    val minutes = (timeLeft / 1000 / 60) % 60
    val seconds = (timeLeft / 1000) % 60

    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Voting ends in:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                color = Color.Gray
            )
        )
        Text(
            text = timeText,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                fontSize = 28.sp // Larger font size for emphasis
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }

}
@Composable
fun CountdownTimerTopBar(timeLeft: Long, modifier: Modifier = Modifier) {
    val hours = (timeLeft / 1000 / 60 / 60) % 24
    val minutes = (timeLeft / 1000 / 60) % 60
    val seconds = (timeLeft / 1000) % 60

    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    Text(
        text = timeText,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Green,
            fontSize = 16.sp // Larger font size for emphasis
        ),
        modifier = Modifier
            .padding(top = 8.dp)
            .background(Color.Black)
    )
}

@Composable
fun TimerScreenTopBar(modifier: Modifier = Modifier) {
    var votingEndTime by remember { mutableStateOf(0L) }
    var timeLeft by remember { mutableStateOf(0L) }
    var isVotingAllowed by remember { mutableStateOf(false) }
    var isVotingActive by remember { mutableStateOf(false) }

    // Listen for changes in Firebase data
    FirebaseVotingEndTime { endTime, votingAllowed ->
        votingEndTime = endTime
        timeLeft = votingEndTime - System.currentTimeMillis()
        isVotingAllowed = votingAllowed

        // If voting is allowed and the end time is in the future, start the countdown
        if (isVotingAllowed && timeLeft > 0) {
            countdown(
                initialTime = timeLeft,
                onTimeChange = { updatedTime ->
                    timeLeft = updatedTime
                    Log.d("Timer", "Time left: $timeLeft ms")
                },
                onVotingEnd = {
                    isVotingActive = false
                    Log.d("Timer", "Voting ended.")
                }
            )
            isVotingActive = true
        } else {
            isVotingActive = false
            Log.d("Timer", "Voting is not active or countdown expired.")
        }
    }

    // Only show the countdown if voting is allowed and active
    if (isVotingAllowed && isVotingActive) {
        CountdownTimerTopBar(timeLeft, modifier)  // Display the countdown timer
    } else {
        Log.d("Timer", "Voting is either not allowed or inactive.")
    }
}
@Composable
fun FirebaseVotingEndTime(onDataFetched: (Long, Boolean) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference.child("voting_management")

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Fetch voting end time and isVotingAllowed flag
                val votingEndTime = snapshot.child("votingEndTime").getValue(Long::class.java) ?: 0L
                val isVotingAllowed = snapshot.child("isVotingAllowed").getValue(Boolean::class.java) ?: false

                // Pass the fetched data to the composable function
                onDataFetched(votingEndTime, isVotingAllowed)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching voting data: ${error.message}")
            }
        })
    }
}


@SuppressLint("DefaultLocale")

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    // Initialize ViewModel
    val newsViewModel: NewsViewModel = viewModel()

    // Fetch news on first composition
    LaunchedEffect(Unit) {
        newsViewModel.fetchNews()
        // Fetch votes if necessary
    }

    val scrollState = rememberScrollState()
    // Countdown Timer (no padding needed to avoid vertical stacking)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState) // Makes the column scrollable
            .padding(paddingValues)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally // Centers the content horizontally
    ) {
        val customFont = FontFamily(Font(R.font.rubix))
        TimerScreen(modifier = Modifier)

        // *** News Section ***
        Text(
            text = "News",
            fontFamily = customFont,
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 36.sp,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Optional padding
                .border(2.dp, Color.Gray) // Optional border
                .background(Color.LightGray) // Optional background color
        ) {
            // Display News gallery content
            NewsHorizontalGallery(viewModel = newsViewModel)
        }


        Spacer(modifier = Modifier.height(16.dp)) // Add spacing between sections

        // *** Statistics Section ***
        Text(
            text = "Statistics",
            fontFamily = customFont,
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 36.sp,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )

        // Display statistics content (e.g., vote results)
        FetchVotesFromFirebase(paddingValues)
    }
}


@Composable
fun VoteScreen(paddingValues: PaddingValues) {
    // Content for Vote
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Vote Screen")
    }
}

@Composable
fun StatisticsScreen(paddingValues: PaddingValues) {
    // Content for Statistics
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Statistics Screen")
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


// The function that sets up the UI
@Composable
fun MyApp() {
    val viewModel: NewsViewModel = viewModel()

    // Fetch news when the app starts
    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    NewsHorizontalGallery(viewModel = viewModel) // Showing horizontally sliding gallery
}

// Data Classes
data class NewsResponse(val articles: List<Article>)
data class Article(
    val title: String,
    val urlToImage: String?,
    val description: String,
    val url: String // URL to open in browser
)

// Retrofit Service
interface NewsService {
    @GET("v2/top-headlines")
    suspend fun getPoliticalNews(
        @Query("category") category: String = "politics",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}

object RetrofitInstance {
    val api: NewsService by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)
    }
}

// ViewModel and Repository
class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    val newsLiveData = repository.newsLiveData

    fun fetchNews() {
        viewModelScope.launch {
            repository.fetchPoliticalNews()
        }
    }
}

class NewsRepository {
    val newsLiveData = MutableLiveData<List<Article>>()

    suspend fun fetchPoliticalNews() {
        val response = RetrofitInstance.api.getPoliticalNews(apiKey = "0895bd291a4c437b8bfbb6c145dabd65")
        newsLiveData.postValue(response.articles.take(10)) // Restrict to 5 articles
    }
}

// Compose UI for displaying horizontally sliding gallery
@Composable
fun NewsHorizontalGallery(viewModel: NewsViewModel) {
    val articles by viewModel.newsLiveData.observeAsState(emptyList())
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var currentIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Automatically scroll forward through the articles
    LaunchedEffect(key1 = articles, key2 = currentIndex) {
        if (articles.isNotEmpty()) {
            scope.launch {
                delay(3000L) // Wait for 3 seconds before scrolling
                if (currentIndex < articles.size - 1) {
                    currentIndex++ // Move to the next article
                } else {
                    currentIndex = 0 // Reset to the first article when we reach the end
                }
                listState.animateScrollToItem(currentIndex) // Scroll to the article
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), // Adjust height for slideshow feel
            state = listState // Attach list state to LazyRow
        ) {
            items(articles) { article ->
                NewsItemCardFancy(
                    article = article,
                    onClick = {
                        // Open the news article URL in a browser when clicked
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun NewsItemCardFancy(article: Article, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(400.dp) // Width for a slideshow feel
            .height(300.dp) // Uniform height
            .clickable { onClick() }
            .shadow(8.dp) // Shadow for depth effect
    ) {
        // Background Image
        article.urlToImage?.let { url ->
            Image(
                painter = rememberImagePainter(url), // Load image
                contentDescription = null,
                contentScale = ContentScale.Crop, // Crop the image
                modifier = Modifier
                    .fillMaxSize() // Fill the entire Box
                    .border(4.dp, Color.White) // Add a border with thickness and color
            )
        }

        // Overlay for gradient effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 100f // Control the gradient overlay
                    )
                )
        )

        // Text and source over the gradient
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp) // Padding within the box for text
        ) {
            Text(
                text = article.title,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.title.substringBefore(" - ") ?: "Unknown", // Assuming source is a string
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun VoteSlideshow(
    nationalCompensatoryVotes: List<CandidateVotes>,
    nationalRegionalVotes: Map<String, List<CandidateVotes>>,
    provincialLegislatureVotes: Map<String, List<CandidateVotes>>
) {
    val sections = listOf(
        "National Compensatory Votes" to nationalCompensatoryVotes.takeTop5(),
        "National Regional Votes" to nationalRegionalVotes,
        "Provincial Legislature Votes" to provincialLegislatureVotes
    )

    var currentSectionIndex by remember { mutableStateOf(0) }

    // Automatically switch slides every few seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Change slide every 3 seconds
            currentSectionIndex = (currentSectionIndex + 1) % sections.size
        }
    }

    // Get current section
    val currentSection = sections[currentSectionIndex]
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentSection.first,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

        // Handle top 5 display for different sections
        when (currentSection.second) {
            is List<*> -> {
                // National Compensatory Votes (List of candidates)
                DisplayProgressBarsForCategory(currentSection.second as List<CandidateVotes>)
            }
            is Map<*, *> -> {
                // National Regional or Provincial Legislature Votes (Map of provinces to candidates)
                (currentSection.second as Map<String, List<CandidateVotes>>).forEach { (region, votes) ->
                    Text(
                        text = region,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    DisplayProgressBarsForCategory(votes.takeTop5())  // Show top 5 for each region
                }
            }
        }
    }
}

@Composable
fun DisplayCandidateVoteCard(candidateVotes: CandidateVotes) {
    // Calculate total votes dynamically from the passed candidate votes
    val totalVotes = candidateVotes.votes // Assuming you have the total votes count available

    // Calculate vote percentage
    val votePercentage = if (totalVotes > 0) candidateVotes.votes.toFloat() / totalVotes else 0f

    // Card for candidate info and progress
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Adjusted padding around each card
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)), // Subtle shadow and rounded corners
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Add internal padding for better spacing
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), // Make the row take up the full width
                horizontalArrangement = Arrangement.SpaceBetween // Space out the elements
            ) {
                // Candidate name
                Text(
                    text = candidateVotes.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),  // Bolder text for candidate name
                    modifier = Modifier.padding(bottom = 8.dp) // Padding between name and progress bar
                )

                // Percentage Text
                Text(
                    text = "${(votePercentage * 100).toInt()}%", // Display percentage as text
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(top = 8.dp) // Padding between progress bar and percentage
                )
            }

            // Progress bar showing percentage of votes with custom styling
            LinearProgressIndicator(
                progress = votePercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp) // Slightly increased height for better visibility
                    .clip(RoundedCornerShape(12.dp)) // Add rounded corners
                    .background(Color.LightGray.copy(alpha = 0.2f)), // Background color for unfilled part
                color = when {
                    votePercentage >= 0.75f -> Color(0xFF4CAF50) // Dark green for high percentages
                    votePercentage >= 0.5f -> Color(0xFF8BC34A) // Light green for moderate percentages
                    votePercentage >= 0.25f -> Color(0xFFFFC107) // Amber for mid-low percentages
                    else -> Color(0xFFF44336) // Red for low percentages
                }, // Dynamic color based on percentage
                trackColor = Color.Gray.copy(alpha = 0.3f)  // Track color for the unfilled portion
            )
        }
    }
}
@Composable
fun FetchVotesFromFirebase(paddingValues: PaddingValues) {
    // State to hold vote data
    var nationalCompensatoryVotes by remember { mutableStateOf(listOf<CandidateVotes>()) }
    var nationalRegionalVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }
    var provincialLegislatureVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }

    // Firebase listeners to fetch vote data
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("Votes")

        database.child("nationalCompensatoryVotes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedVotes = snapshot.children.map { dataSnapshot ->
                    CandidateVotes(
                        name = dataSnapshot.key.toString(),
                        votes = dataSnapshot.value.toString().toInt()
                    )
                }
                nationalCompensatoryVotes = fetchedVotes
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        database.child("nationalRegionalVotes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val regionVotes = snapshot.children.associate { regionSnapshot ->
                    regionSnapshot.key.toString() to regionSnapshot.children.map { candidateSnapshot ->
                        CandidateVotes(
                            name = candidateSnapshot.key.toString(),
                            votes = candidateSnapshot.value.toString().toInt()
                        )
                    }
                }
                nationalRegionalVotes = regionVotes
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        database.child("provincialLegislatureVotes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val legislatureVotes = snapshot.children.associate { regionSnapshot ->
                    regionSnapshot.key.toString() to regionSnapshot.children.map { candidateSnapshot ->
                        CandidateVotes(
                            name = candidateSnapshot.key.toString(),
                            votes = candidateSnapshot.value.toString().toInt()
                        )
                    }
                }
                provincialLegislatureVotes = legislatureVotes
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Display the slideshow of vote results with padding applied
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Box with shadow and rounded corners for the slideshow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp) // Ensure no vertical padding
                .shadow(4.dp, shape = RoundedCornerShape(12.dp)) // Shadow effect
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .background(Color.White) // Background color
        ) {
            VoteSlideshow(nationalCompensatoryVotes, nationalRegionalVotes, provincialLegislatureVotes)
        }
    }
}

// Helper function to get top 5 votes
fun List<CandidateVotes>.takeTop5(): List<CandidateVotes> {
    return this.sortedByDescending { it.votes }.take(5)
}

// Helper function to flatten regional or provincial votes to top 5
fun Map<String, List<CandidateVotes>>.flattenToTop5(): List<CandidateVotes> {
    return this.values.flatten().sortedByDescending { it.votes }.take(5)
}


// Preview function to see the UI in Android Studio's preview window

@Composable
fun PreviewMyApp() {
    // Dummy data for preview
    val dummyViewModel = NewsViewModel().apply {
        newsLiveData.value = listOf(
            Article("Preview Title 1", null, "Preview Description 1", "https://example.com"),
            Article("Preview Title 2", null, "Preview Description 2", "https://example.com"),
            Article("Preview Title 3", null, "Preview Description 3", "https://example.com"),
            Article("Preview Title 4", null, "Preview Description 4", "https://example.com"),
            Article("Preview Title 5", null, "Preview Description 5", "https://example.com")
        )
    }
    NewsHorizontalGallery(viewModel = dummyViewModel)
}

