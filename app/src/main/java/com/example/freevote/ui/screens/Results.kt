package com.example.freevote.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

@Composable
fun ResultsScreen(paddingValues: PaddingValues) {
    val scrollState = rememberScrollState()
    var votingEndTime by remember { mutableStateOf(0L) }
    var timeLeft by remember { mutableStateOf(0L) }
    var isVotingActive by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("voting_management")
        database.child("votingEndTime").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                votingEndTime = snapshot.getValue(Long::class.java) ?: 0L
                timeLeft = votingEndTime - System.currentTimeMillis() - 1000
                if (timeLeft > 0) {
                    countdown(
                        initialTime = timeLeft,
                        onTimeChange = { updatedTime -> timeLeft = updatedTime },
                        onVotingEnd = { isVotingActive = false }
                    )
                } else {
                    isVotingActive = false
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Use Box to set a white background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background color to white
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isVotingActive) {
                CountdownTimer(timeLeft)
            }

            FetchVotesFromFirebase()
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimer(timeLeft: Long) {
    val hours = (timeLeft / 1000 / 60 / 60) % 24
    val minutes = (timeLeft / 1000 / 60) % 60
    val seconds = (timeLeft / 1000) % 60

    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Text(
        text = timeText,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Red
        ),
        modifier = Modifier.padding(16.dp)
    )
}

data class CandidateVotes(val name: String, val votes: Int)

@Composable
fun FetchVotesFromFirebase() {
    var nationalCompensatoryVotes by remember { mutableStateOf(listOf<CandidateVotes>()) }
    var nationalRegionalVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }
    var provincialLegislatureVotes by remember { mutableStateOf(mapOf<String, List<CandidateVotes>>()) }

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

    DisplayVoteResults(nationalCompensatoryVotes, nationalRegionalVotes, provincialLegislatureVotes)
}

@Composable
fun DisplayVoteResults(
    nationalCompensatoryVotes: List<CandidateVotes>,
    nationalRegionalVotes: Map<String, List<CandidateVotes>>,
    provincialLegislatureVotes: Map<String, List<CandidateVotes>>
) {
    // Column fills the maximum size with a white background
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(Color.White) // Set the overall background to white
    ) {
        // National Compensatory Votes Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Optional padding to ensure the shadow is visible
                .drawBehind {
                    // Softer, brusher shadow appearance
                    val shadowColor = Color.Black.copy(alpha = 0.2f) // More transparent shadow
                    val cornerRadius = 12.dp.toPx() // Same corner radius as the card

                    // Draw shadow all around the card by centering it around the card edges
                    drawRoundRect(
                        color = shadowColor,
                        topLeft = Offset(x = -8.dp.toPx(), y = -8.dp.toPx()), // Move shadow up and left
                        size = Size(
                            width = this.size.width + 16.dp.toPx(), // Add shadow on both sides horizontally
                            height = this.size.height + 16.dp.toPx() // Add shadow on both sides vertically
                        ),
                        cornerRadius = CornerRadius(cornerRadius),
                      // Use Fill for a smoother gradient-like fill
                    )

                    // Add a second shadow layer for more depth (optional)
                    drawRoundRect(
                        color = shadowColor.copy(alpha = 0.1f), // Lighter second shadow
                        topLeft = Offset(x = -4.dp.toPx(), y = -4.dp.toPx()), // Slightly smaller offset
                        size = Size(
                            width = this.size.width + 8.dp.toPx(), // Smaller size for a layered effect
                            height = this.size.height + 8.dp.toPx()
                        ),
                        cornerRadius = CornerRadius(cornerRadius)
                    )
                },
            shape = RoundedCornerShape(12.dp), // Match card shape with shadow corners
            elevation = CardDefaults.cardElevation(0.dp), // Disable built-in elevation (we use custom shadow)
            colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Keep card transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Ensure Box has a white background
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "National Compensatory Votes",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Rank and display candidates
                    DisplayProgressBarsForCategory(nationalCompensatoryVotes)
                }
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        // National Regional Votes Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Optional padding to ensure the shadow is visible
                .drawBehind {
                    // Softer, brusher shadow appearance
                    val shadowColor = Color.Black.copy(alpha = 0.2f) // More transparent shadow
                    val cornerRadius = 12.dp.toPx() // Same corner radius as the card

                    // Draw shadow all around the card by centering it around the card edges
                    drawRoundRect(
                        color = shadowColor,
                        topLeft = Offset(x = -8.dp.toPx(), y = -8.dp.toPx()), // Move shadow up and left
                        size = Size(
                            width = this.size.width + 16.dp.toPx(), // Add shadow on both sides horizontally
                            height = this.size.height + 16.dp.toPx() // Add shadow on both sides vertically
                        ),
                        cornerRadius = CornerRadius(cornerRadius),
                        // Use Fill for a smoother gradient-like fill
                    )

                    // Add a second shadow layer for more depth (optional)
                    drawRoundRect(
                        color = shadowColor.copy(alpha = 0.1f), // Lighter second shadow
                        topLeft = Offset(x = -4.dp.toPx(), y = -4.dp.toPx()), // Slightly smaller offset
                        size = Size(
                            width = this.size.width + 8.dp.toPx(), // Smaller size for a layered effect
                            height = this.size.height + 8.dp.toPx()
                        ),
                        cornerRadius = CornerRadius(cornerRadius)
                    )
                },
            shape = RoundedCornerShape(12.dp), // Match card shape with shadow corners
            elevation = CardDefaults.cardElevation(0.dp), // Disable built-in elevation (we use custom shadow)
            colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Keep card transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Ensure Box has a white background
                    .padding(16.dp)
            ) {
                Column {
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
                        // Rank and display candidates
                        DisplayProgressBarsForCategory(candidates)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Provincial Legislature Votes Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Optional padding to ensure the shadow is visible
                .drawBehind {
                    // Softer, brusher shadow appearance
                    val shadowColor = Color.Black.copy(alpha = 0.2f) // More transparent shadow
                    val cornerRadius = 12.dp.toPx() // Same corner radius as the card

                    // Draw shadow all around the card by centering it around the card edges
                    drawRoundRect(
                        color = shadowColor,
                        topLeft = Offset(x = -8.dp.toPx(), y = -8.dp.toPx()), // Move shadow up and left
                        size = Size(
                            width = this.size.width + 16.dp.toPx(), // Add shadow on both sides horizontally
                            height = this.size.height + 16.dp.toPx() // Add shadow on both sides vertically
                        ),
                        cornerRadius = CornerRadius(cornerRadius),
                        // Use Fill for a smoother gradient-like fill
                    )

                    // Add a second shadow layer for more depth (optional)
                    drawRoundRect(
                        color = shadowColor.copy(alpha = 0.1f), // Lighter second shadow
                        topLeft = Offset(x = -4.dp.toPx(), y = -4.dp.toPx()), // Slightly smaller offset
                        size = Size(
                            width = this.size.width + 8.dp.toPx(), // Smaller size for a layered effect
                            height = this.size.height + 8.dp.toPx()
                        ),
                        cornerRadius = CornerRadius(cornerRadius)
                    )
                },
            shape = RoundedCornerShape(12.dp), // Match card shape with shadow corners
            elevation = CardDefaults.cardElevation(0.dp), // Disable built-in elevation (we use custom shadow)
            colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Keep card transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Ensure Box has a white background
                    .padding(16.dp)
            ) {
                Column {
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
                        // Rank and display candidates
                        DisplayProgressBarsForCategory(candidates)
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayProgressBarsForCategory(candidateVotes: List<CandidateVotes>) {
    // Calculate total votes
    val totalVotes = candidateVotes.sumOf { it.votes }

    // Create a list of candidates with their vote percentage and rank
    val rankedCandidates = candidateVotes.map { candidate ->
        val votePercentage = if (totalVotes > 0) candidate.votes.toFloat() / totalVotes else 0f
        candidate to (votePercentage * 100)
    }.sortedByDescending { it.second } // Sort by vote percentage

    // Find the highest and lowest percentages
    val highestPercentage = rankedCandidates.maxOfOrNull { it.second } ?: 0f
    val lowestPercentage = rankedCandidates.minOfOrNull { it.second } ?: 0f

    // Display each ranked candidate
    rankedCandidates.forEach { (candidate, percentage) ->
        val votePercentage = percentage / 100 // Convert percentage back to a decimal for progress bar

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .shadow(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), // Ensure the row takes the full width
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    BasicText(
                        text = candidate.name,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    BasicText(
                        text = String.format("%.2f%%", percentage),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp) // Optional: Add space to the start of this text
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
                    color = when (percentage) {
                        highestPercentage -> Color(0xFF4CAF50) // Green for highest percentage
                        lowestPercentage -> Color(0xFFF44336) // Red for lowest percentage
                        else -> Color.Yellow // Yellow for the rest
                    },
                    trackColor = Color.Gray.copy(alpha = 0.3f)  // Track color for the unfilled portion
                )
            }
        }
    }
}

