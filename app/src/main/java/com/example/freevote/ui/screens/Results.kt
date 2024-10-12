package com.example.freevote.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.database.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.rubikMoonrocksFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Define countdown as a top-level function
fun countdown(
    initialTime: Long,
    onTimeChange: (Long) -> Unit,
    onVotingEnd: () -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Main) // Use Main dispatcher for UI updates
    scope.launch {
        var remainingTime = initialTime

        while (remainingTime > 0) {
            delay(1000) // Wait for 1 second
            remainingTime -= 1000 // Decrease by 1 second
            onTimeChange(remainingTime) // Update time left
        }
        onVotingEnd() // Mark voting as inactive
    }
}

@Composable
fun ResultsScreen(navController: NavController, viewModel: MainViewModel) {
    // Scrollable state
    val scrollState = rememberScrollState()

    // State to hold the end time and countdown
    var votingEndTime by remember { mutableStateOf(0L) }
    var timeLeft by remember { mutableStateOf(0L) }
    var isVotingActive by remember { mutableStateOf(true) }

    // Fetch votingEndTime from Firebase
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("voting_management")
        database.child("votingEndTime").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                votingEndTime = snapshot.getValue(Long::class.java) ?: 0L
                // Calculate initial time left
                timeLeft = votingEndTime - System.currentTimeMillis()-2000

                // Start countdown if the voting is active
                if (timeLeft > 0) {
                    countdown(
                        initialTime = timeLeft,
                        onTimeChange = { updatedTime -> timeLeft = updatedTime }, // Update timeLeft
                        onVotingEnd = { isVotingActive = false } // End voting
                    )
                } else {
                    isVotingActive = false // Voting has already ended
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Enable vertical scrolling
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Text header
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
            ),
            modifier = Modifier.padding(16.dp)
        )

        // Countdown Timer
        if (isVotingActive) {
            CountdownTimer(timeLeft) // Show countdown if voting is active
        }

        FetchVotesFromFirebase() // This will also be included in the scrollable content
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimer(timeLeft: Long) {
    // Calculate hours, minutes, and seconds
    val hours = (timeLeft / 1000 / 60 / 60) % 24
    val minutes = (timeLeft / 1000 / 60) % 60
    val seconds = (timeLeft / 1000) % 60

    // Format the countdown text
    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Text(
        text = timeText,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Red // Change color as needed
        ),
        modifier = Modifier.padding(16.dp) // Add some padding for better visual
    )
}


//Text(text = "Results Screen")
//Button(onClick = { /*TODO*/
//  navController.navigate("homenews")}) {
//Text(text = "go home")
// }
// Data class to store votes for a candidate
data class CandidateVotes(val name: String, val votes: Int)

@Composable
fun FetchVotesFromFirebase() {
    // State to hold vote data
    var nationalCompensatoryVotes by remember { mutableStateOf(listOf<CandidateVotes>()) }
    var nationalRegionalVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }
    var provincialLegislatureVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }

    // Listen for real-time updates from Firebase
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("Votes")

        // Real-time listener for National Compensatory Votes
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

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Real-time listener for National Regional Votes
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

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Real-time listener for Provincial Legislature Votes
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

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Display the votes with real-time updates
    DisplayVoteResults(nationalCompensatoryVotes, nationalRegionalVotes, provincialLegislatureVotes)
}


@Composable
fun DisplayVoteResults(
    nationalCompensatoryVotes: List<CandidateVotes>,
    nationalRegionalVotes: Map<String, List<CandidateVotes>>,
    provincialLegislatureVotes: Map<String, List<CandidateVotes>>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // National Compensatory Votes Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5)), // Light background for the card
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Updated elevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "National Compensatory Votes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DisplayProgressBarsForCategory(nationalCompensatoryVotes)
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between sections

        // National Regional Votes Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5)), // Light background for the card
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Updated elevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "National Regional Votes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                nationalRegionalVotes.forEach { (region, candidates) ->
                    Text(
                        text = "$region:",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    DisplayProgressBarsForCategory(candidates)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between sections

        // Provincial Legislature Votes Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5)), // Light background for the card
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Updated elevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Provincial Legislature Votes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                provincialLegislatureVotes.forEach { (region, candidates) ->
                    Text(
                        text = "$region:",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    DisplayProgressBarsForCategory(candidates)
                }
            }
        }
    }
}


@Composable
fun DisplayProgressBarsForCategory(candidateVotes: List<CandidateVotes>) {
    val totalVotes = candidateVotes.sumOf { it.votes }

    candidateVotes.forEach { candidate ->
        val votePercentage = if (totalVotes > 0) candidate.votes.toFloat() / totalVotes else 0f

        // Card for candidate info and progress
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Adjusted padding around each card
                .shadow(4.dp, shape = RoundedCornerShape(12.dp)), // Subtle shadow and rounded corners
           // shape = RoundedCornerShape(12.dp) // Rounded corners for card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp) // Add internal padding for better spacing
                    .background(Color(0xFFf5f5f5)), // Light background color for card
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), // Make the row take up the full width
                    horizontalArrangement = Arrangement.SpaceBetween // Space out the elements
                ) {
                    // Candidate name (no votes displayed)
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),  // Bolder text for candidate name
                        modifier = Modifier.padding(bottom = 8.dp) // Padding between name and progress bar
                    )

                    // Percentage Text
                    Text(
                        text = "${(votePercentage * 100).toInt()}%", // Display percentage as text
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier
                            .padding(top = 8.dp) // Padding between progress bar and percentage
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
}

