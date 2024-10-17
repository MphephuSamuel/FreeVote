package com.example.freevote.ui.screens
import com.google.firebase.database.ServerValue

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.UUID



val AbhayaLibreExtraBold = FontFamily(
    Font(R.font.abhaya_libre_extrabold)
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VotePage(modifier: Modifier, navController: NavController, viewModel: MainViewModel) {
    // Define the custom font
    val Rubikmoonroocks = FontFamily(Font(R.font.rubik_moonrocks))
    val provinces = listOf(
        "Eastern Cape",
        "Free State",
        "Gauteng",
        "KwaZulu-Natal",
        "Limpopo",
        "Mpumalanga",
        "North West",
        "Northern Cape",
        "Western Cape"
    )

    // State variable to hold the selected province
    var selectedProvince by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
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
            fontFamily = Rubikmoonroocks,
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
        TimerScreen(modifier = Modifier)

        // Location selection section
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RectangleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Location",
                    fontFamily = AbhayaLibreExtraBold,
                    fontSize = 32.sp
                )
                // Call LocationDropdown here with provinces list
                LocationDropdown(provinces) { province ->
                    selectedProvince = province // Update selected province
                }
            }
        }

        // Spacer between sections
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )

        // Vote section
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RectangleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vote",
                    fontFamily = AbhayaLibreExtraBold,
                    fontSize = 32.sp
                )
                // Pass the selected province to VoteDropdownMenu
                VoteDropdownMenu(selectedProvince)
            }
        }
    }
}



@Composable
fun LocationDropdown(provinces: List<String>, onProvinceSelected: (String) -> Unit) {
    // State variables for managing dropdown visibility and selected item
    var expandedProvince by remember { mutableStateOf(false) }
    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var locationConfirmation by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        // Province Dropdown
        Button(
            onClick = { expandedProvince = !expandedProvince },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = selectedProvince ?: "Select Province")
        }

        DropdownMenu(
            expanded = expandedProvince,
            onDismissRequest = { expandedProvince = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            provinces.forEachIndexed {index, province ->
                DropdownMenuItem(
                    text = {
                        Text(province)
                    },
                    onClick = {
                        selectedProvince = province
                        expandedProvince = false // Close the dropdown after selection
                        onProvinceSelected(province) // Trigger the callback
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < provinces.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun VoteDropdownMenu(selectedProvince: String?) {
    // State variables for each dropdown
    var expandedNationalVote by remember { mutableStateOf(false) }
    var expandedNational by remember { mutableStateOf(false) }
    var expandedRegional by remember { mutableStateOf(false) }
    var expandedProvincial by remember { mutableStateOf(false) }

    // State variables to hold the selected PartyOption objects
    var selectedNational by remember { mutableStateOf<PartyOption?>(null) }
    var selectedRegional by remember { mutableStateOf<PartyOption?>(null) }
    var selectedProvincial by remember { mutableStateOf<PartyOption?>(null) }

    // Firebase Realtime Database instance
    val database = Firebase.database.reference

    // State variables to hold the candidates fetched from Firebase
    var nationalOptions = remember { mutableStateListOf<PartyOption>() }
    var regionalOptions = remember { mutableStateListOf<PartyOption>() }
    var provincialOptions = remember { mutableStateListOf<PartyOption>() }

    val context = LocalContext.current


    // Fetch national compensatory candidates from Firebase
    LaunchedEffect(Unit) {
        database.child("ballots/national_compensatory/candidates")
            .get().addOnSuccessListener { dataSnapshot ->
                nationalOptions.clear() // Clear existing data
                nationalOptions.addAll(dataSnapshot.children.map { candidate ->
                    PartyOption(
                        name = candidate.child("party_name").getValue(String::class.java) ?: "",
                        leaderFaceUrl = candidate.child("party_leader_image_url").getValue(String::class.java) ?: "",
                        abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                        logoUrl = candidate.child("party_logo_url").getValue(String::class.java) ?: ""
                    )
                })
            }.addOnFailureListener { exception ->
                // Handle possible errors
            }
    }


    // Fetch regional candidates based on selected province
    LaunchedEffect(selectedProvince) {
        if (selectedProvince != null) {
            database.child("ballots/national_regional/${selectedProvince}/candidates")
                .get().addOnSuccessListener { dataSnapshot ->
                    regionalOptions.clear() // Clear existing data
                    regionalOptions.addAll(dataSnapshot.children.map { candidate ->
                        PartyOption(
                            name = candidate.child("party_name").getValue(String::class.java) ?: "",
                            leaderFaceUrl = candidate.child("party_leader_image_url").getValue(String::class.java) ?: "",
                            abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                            logoUrl = candidate.child("party_logo_url").getValue(String::class.java) ?: ""
                        )
                    })
                }.addOnFailureListener { exception ->
                    // Handle possible errors
                }
        }
    }

    // Fetch provincial candidates based on selected province
    LaunchedEffect(selectedProvince) {
        if (selectedProvince != null) {
            database.child("ballots/provincial_legislature/${selectedProvince}/candidates")
                .get().addOnSuccessListener { dataSnapshot ->
                    provincialOptions.clear() // Clear existing data
                    provincialOptions.addAll(dataSnapshot.children.map { candidate ->
                        PartyOption(
                            name = candidate.child("party_name").getValue(String::class.java) ?: "",
                            leaderFaceUrl = candidate.child("party_leader_image_url").getValue(String::class.java) ?: "",
                            abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                            logoUrl = candidate.child("party_logo_url").getValue(String::class.java) ?: ""
                        )
                    })
                }.addOnFailureListener { exception ->
                    // Handle possible errors
                }
        }
    }

    // Dropdown UI components for voting
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // National Vote Dropdown
        Button(
            onClick = { expandedNationalVote = !expandedNationalVote },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = "National")
        }

        DropdownMenu(
            expanded = expandedNationalVote,
            onDismissRequest = { expandedNationalVote = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff04a0df))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xff04a0df), Color.White)
                    )
                )
                .padding(8.dp)
        ) {
            nationalOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Party leader image
                            Image(
                                painter = rememberImagePainter(option.leaderFaceUrl),
                                contentDescription = "Party Leader",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            // Party details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(text = option.name, fontWeight = FontWeight.Bold)
                                Text(text = option.abbreviation, color = Color.Gray)
                            }
                            // Party logo
                            Image(
                                painter = rememberImagePainter(option.logoUrl),
                                contentDescription = "Party Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            CrossCheckbox(
                                checked = option.name == selectedNational?.name, // Compare option.name with selectedNational?.name
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedNational = option // Assign the whole PartyOption instead of option.name
                                        expandedNational = false
                                        expandedNationalVote = true
                                    }
                                }
                            )

                        }
                    },
                    onClick = {
                        selectedNational = option // Assign the PartyOption instead of just the name
                        expandedNational = false
                        expandedNationalVote = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < nationalOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // Regional Vote Button
        Button(
            onClick = { expandedRegional = !expandedRegional },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = "Regional")
        }

// Regional Vote Dropdown
        DropdownMenu(
            expanded = expandedRegional,
            onDismissRequest = { expandedRegional = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff04a0df))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFF7F50), Color.White)
                    )
                )
                .padding(8.dp)
        ) {
            regionalOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Party leader image
                            Image(
                                painter = rememberImagePainter(option.leaderFaceUrl),
                                contentDescription = "Party Leader",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            // Party details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(text = option.name, fontWeight = FontWeight.Bold)
                                Text(text = option.abbreviation, color = Color.Gray)
                            }
                            // Party logo
                            Image(
                                painter = rememberImagePainter(option.logoUrl),
                                contentDescription = "Party Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            CrossCheckbox(
                                checked = option.name == selectedRegional?.name,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedRegional = option
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedRegional = option
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < regionalOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }


        // Provincial Vote Dropdown
        Button(
            onClick = { expandedProvincial = !expandedProvincial },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = "Provincial")
        }

        DropdownMenu(
            expanded = expandedProvincial,
            onDismissRequest = { expandedProvincial = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff04a0df))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xffcc3366), Color.White)
                    )
                )
                .padding(8.dp)
        ) {
            provincialOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Party leader image
                            Image(
                                painter = rememberImagePainter(option.leaderFaceUrl),
                                contentDescription = "Party Leader",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            // Party details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(text = option.name, fontWeight = FontWeight.Bold)
                                Text(text = option.abbreviation, color = Color.Gray)
                            }
                            // Party logo
                            Image(
                                painter = rememberImagePainter(option.logoUrl),
                                contentDescription = "Party Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            CrossCheckbox(
                                checked = option.name == selectedProvincial?.name,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedProvincial = option
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedProvincial = option
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < provincialOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth() // Fills maximum width
                .background(Color.LightGray) // Set the background color
                .padding(8.dp) // Add padding
        ) {
            Row {
                Text(
                    text = "Selected National: ",
                    color = Color.Gray, // Label color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(
                    text = selectedNational?.name ?: "",
                    color = Color.Blue, // Candidate name color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth() // Fills maximum width
                .background(Color.LightGray) // Set the background color
                .padding(8.dp) // Add padding
        ) {
            Row {
                Text(
                    text = "Selected Regional: ",
                    color = Color.Gray, // Label color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(
                    text = selectedRegional?.name ?: "",
                    color = Color.Green, // Candidate name color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth() // Fills maximum width
                .background(Color.LightGray) // Set the background color
                .padding(8.dp) // Add padding
        ) {
            Row {
                Text(
                    text = "Selected Provincial: ",
                    color = Color.Gray, // Label color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(
                    text = selectedProvincial?.name ?: "",
                    color = Color.Red, // Candidate name color
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }


        Button(
            onClick = {
                val userId = FirebaseAuth.getInstance().currentUser?.uid // Get the current user's ID
                if (userId != null) {
                    val voteStatusRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("votes/$userId")

                    // Check if the user has already voted
                    voteStatusRef.child("hasVoted").get().addOnSuccessListener { dataSnapshot ->
                        val hasVoted = dataSnapshot.getValue(Boolean::class.java) ?: false

                        if (!hasVoted) {
                            val votingManagementRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("voting_management")

                            // Check the voting status before proceeding to cast the vote
                            votingManagementRef.child("isVotingAllowed").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val isVotingAllowed = snapshot.getValue(Boolean::class.java) ?: false
                                    if (isVotingAllowed) {
                                        // Proceed to cast the vote
                                        castVote(
                                            nationalVote = selectedNational,
                                            regionalVote = selectedRegional,
                                            provincialVote = selectedProvincial,
                                            selectedProvince = selectedProvince, // Pass the selected province here
                                            onVoteSuccess = {
                                                // Handle successful vote storage
                                                Toast.makeText(context, "Vote cast successfully!", Toast.LENGTH_SHORT).show()

                                                // Update the user's voting status to true
                                                voteStatusRef.child("hasVoted").setValue(true)

                                                // Optionally clear selections
                                                selectedNational = null
                                                selectedRegional = null
                                                selectedProvincial = null
                                            },
                                            onVoteError = { exception ->
                                                // Handle error
                                                Toast.makeText(context, "Error casting vote: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    } else {
                                        // Voting is not allowed
                                        Toast.makeText(context, "Voting time has ended.", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error checking voting status", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            // User has already voted
                            Toast.makeText(context, "You have already voted.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error checking voting status", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // User is not authenticated
                    Toast.makeText(context, "You must be logged in to vote.", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = selectedNational != null || selectedRegional != null || selectedProvincial != null // Enable only if any option is selected
        ) {
            Text(text = "Vote")
        }

    }
}

@Composable
fun CrossCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (checked) Color.Red else Color.White, // Background color
                shape = RoundedCornerShape(4.dp)
            )
            .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
            .clickable(onClick = { onCheckedChange?.invoke(!checked) })
    ) {
        if (checked) {
            Text(
                text = "X",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// Data class definition remains unchanged
data class PartyOption(
    val name: String,                  // The name of the party
    val leaderFaceUrl: String,        // URL for the leader's face image
    val abbreviation: String,          // The party's abbreviation
    val logoUrl: String                // URL for the party logo image
)

fun castVote(
    nationalVote: PartyOption?,
    regionalVote: PartyOption?,
    provincialVote: PartyOption?,
    selectedProvince: String?, // Add selected province to identify regional/provincial votes
    onVoteSuccess: () -> Unit, // Callback for success
    onVoteError: (Exception) -> Unit // Callback for error
) {
    val database = FirebaseDatabase.getInstance().reference

    // Perform Firebase updates for each vote
    val updates = mutableMapOf<String, Any>()

    // Update national compensatory vote count and store additional details
    nationalVote?.let {
        val nationalPath = "Votes/nationalCompensatoryVotes/${it.abbreviation}"
        updates["$nationalPath/voteCount"] = ServerValue.increment(1)
        updates["$nationalPath/party_leader_image_url"] = it.leaderFaceUrl
        updates["$nationalPath/party_logo_url"] = it.logoUrl
    }

    // Update national regional vote count and store additional details
    if (selectedProvince != null && regionalVote != null) {
        val regionalPath = "Votes/nationalRegionalVotes/${selectedProvince}Votes/${regionalVote.abbreviation}"
        updates["$regionalPath/voteCount"] = ServerValue.increment(1)
        updates["$regionalPath/party_leader_image_url"] = regionalVote.leaderFaceUrl
        updates["$regionalPath/party_logo_url"] = regionalVote.logoUrl
    }

    // Update provincial legislature vote count and store additional details
    if (selectedProvince != null && provincialVote != null) {
        val provincialPath = "Votes/provincialLegislatureVotes/${selectedProvince}Votes/${provincialVote.abbreviation}"
        updates["$provincialPath/voteCount"] = ServerValue.increment(1)
        updates["$provincialPath/party_leader_image_url"] = provincialVote.leaderFaceUrl
        updates["$provincialPath/party_logo_url"] = provincialVote.logoUrl
    }

    // Apply all updates to the Firebase database
    database.updateChildren(updates)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onVoteSuccess() // Call success callback
            } else {
                onVoteError(task.exception ?: Exception("Vote casting failed")) // Call error callback with exception
            }
        }
}
