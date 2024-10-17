package com.example.freevote.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*


@Composable
fun ResultsScreen(paddingValues: PaddingValues) {
    val scrollState = rememberScrollState()

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
            TimerScreen(modifier = Modifier)
            FetchVotesFromFirebase()
        }
    }
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
                val fetchedVotes = snapshot.children.mapNotNull { dataSnapshot ->
                    val name = dataSnapshot.key
                    val votes = dataSnapshot.value?.toString()?.toIntOrNull()
                    if (name != null && votes != null) {
                        CandidateVotes(name = name, votes = votes)
                    } else {
                        null // Safeguard against null values
                    }
                }
                nationalCompensatoryVotes = shuffleCandidates(fetchedVotes)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        database.child("nationalRegionalVotes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val regionVotes = snapshot.children.associate { regionSnapshot ->
                    regionSnapshot.key.toString() to regionSnapshot.children.mapNotNull { candidateSnapshot ->
                        val name = candidateSnapshot.key
                        val votes = candidateSnapshot.value?.toString()?.toIntOrNull()
                        if (name != null && votes != null) {
                            CandidateVotes(name = name, votes = votes)
                        } else {
                            null
                        }
                    }.let { shuffleCandidates(it) } // Shuffle candidates for the region
                }
                nationalRegionalVotes = regionVotes
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        database.child("provincialLegislatureVotes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val legislatureVotes = snapshot.children.associate { regionSnapshot ->
                    regionSnapshot.key.toString() to regionSnapshot.children.mapNotNull { candidateSnapshot ->
                        val name = candidateSnapshot.key
                        val votes = candidateSnapshot.value?.toString()?.toIntOrNull()
                        if (name != null && votes != null) {
                            CandidateVotes(name = name, votes = votes)
                        } else {
                            null
                        }
                    }.let { shuffleCandidates(it) } // Shuffle candidates for the region
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        DisplayVotesCard("National Compensatory Votes", nationalCompensatoryVotes)

        Spacer(modifier = Modifier.height(24.dp))

        DisplayRegionalVotesCard(nationalRegionalVotes)

        Spacer(modifier = Modifier.height(24.dp))

        DisplayRegionalVotesCard(provincialLegislatureVotes, "Provincial Legislature Votes")
    }
}

@Composable
fun DisplayVotesCard(title: String, votes: List<CandidateVotes>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .drawBehind { drawCustomShadow() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DisplayProgressBarsForCategory(votes)
            }
        }
    }
}

@Composable
fun DisplayRegionalVotesCard(votes: Map<String, List<CandidateVotes>>, title: String = "National Regional Votes") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .drawBehind { drawCustomShadow() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                votes.forEach { (region, candidates) ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$region:",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF666666)),
                        modifier = Modifier.padding(bottom = 4.dp)
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

    // Calculate candidate percentages and sort
    val rankedCandidates = candidateVotes.map { candidate ->
        val votePercentage = if (totalVotes > 0) candidate.votes.toFloat() / totalVotes else 0f
        candidate to (votePercentage * 100)
    }.sortedByDescending { it.second }

    // Get highest and lowest vote percentages
    val highestPercentage = rankedCandidates.maxOfOrNull { it.second } ?: 0f
    val lowestPercentage = rankedCandidates.minOfOrNull { it.second } ?: 0f

    rankedCandidates.forEachIndexed { index, (candidate, percentage) ->

        val targetProgress = percentage / 100
        val animatedProgress by animateFloatAsState(
            targetValue = targetProgress,
            animationSpec = tween(
                durationMillis = 1000, // Duration for smooth climbing effect
                easing = FastOutSlowInEasing // Easing function for gradual start and finish
            )
        )

        val isLeader = percentage == highestPercentage
        val isLowest = percentage == lowestPercentage

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(3.dp)
                .border(
                    width = 2.dp,
                    color = if (isLeader) Color(0xFF4CAF50) else Color(0xFF000000),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = if (isLeader) Color(0xFFE0F7FA) else Color(0xFFE0E0E0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "${candidate.votes} votes (${String.format("%.2f%%", percentage)})",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isLeader) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = Color(0xFF4F4F4F)
                    )
                }

                LinearProgressIndicator(
                    progress = animatedProgress, // Smoothly animated progress
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF)),
                    color = when {
                        isLeader -> Color(0xFF4CAF50)
                        isLowest -> Color(0xFFF44336)
                        else -> Color(0xFFFFC107)
                    },
                    trackColor = Color(0xFFFFFFFF).copy(alpha = 0.3f),
                )
            }
        }
    }
}

// Function to shuffle candidates based on votes
fun shuffleCandidates(candidates: List<CandidateVotes>): List<CandidateVotes> {
    return candidates.sortedBy { it.votes }.shuffled() // Sort first (if needed) and then shuffle
}

fun DrawScope.drawCustomShadow() {
    val shadowColor = Color.Black.copy(alpha = 0.2f)
    val cornerRadius = 12.dp.toPx()

    // Draw shadow all around the card
    drawRoundRect(
        color = shadowColor,
        topLeft = Offset(x = -8.dp.toPx(), y = -8.dp.toPx()),
        size = Size(
            width = this.size.width + 16.dp.toPx(),
            height = this.size.height + 16.dp.toPx()
        ),
        cornerRadius = CornerRadius(cornerRadius)
    )

    // Add a second shadow layer for more depth
    drawRoundRect(
        color = shadowColor.copy(alpha = 0.1f),
        topLeft = Offset(x = -4.dp.toPx(), y = -4.dp.toPx()),
        size = Size(
            width = this.size.width + 8.dp.toPx(),
            height = this.size.height + 8.dp.toPx()
        ),
        cornerRadius = CornerRadius(cornerRadius)
    )
}
