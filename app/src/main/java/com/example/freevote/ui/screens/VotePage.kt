package com.example.freevote.ui.screens

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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
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
            provinces.forEach { province ->
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
            }
        }

        // Confirm button for selected province
        if (selectedProvince != null) {
            Button(
                onClick = { locationConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Confirm Province")
            }
        }

        // Location confirmation dialog
        if (locationConfirmation) {
            AlertDialog(
                onDismissRequest = { locationConfirmation = false },
                title = { Text("Confirm Location") },
                text = {
                    Text("Province: ${selectedProvince ?: "None"}")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            locationConfirmation = false
                            // Optionally clear the selected province or keep it for further use
                            // selectedProvince = null
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            locationConfirmation = false
                            selectedProvince = null // Clear the selection if cancelled
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
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
    var nationalOptions by remember { mutableStateOf<List<PartyOption>>(emptyList()) }
    var regionalOptions by remember { mutableStateOf<List<PartyOption>>(emptyList()) }
    var provincialOptions by remember { mutableStateOf<List<PartyOption>>(emptyList()) }

    // Fetch national compensatory candidates from Firebase
    LaunchedEffect(Unit) {
        database.child("ballots/national_compensatory/candidates")
            .get().addOnSuccessListener { dataSnapshot ->
                nationalOptions = dataSnapshot.children.map { candidate ->
                    PartyOption(
                        name = candidate.child("party_name").getValue(String::class.java) ?: "",
                        leaderFaceRes = R.drawable.default_leader,
                        abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                        logoRes = R.drawable.default_logo
                    )
                }
            }
    }

    // Fetch regional candidates based on selected province
    LaunchedEffect(selectedProvince) {
        if (selectedProvince != null) {
            database.child("ballots/national_regional/${selectedProvince}/candidates")
                .get().addOnSuccessListener { dataSnapshot ->
                    regionalOptions = dataSnapshot.children.map { candidate ->
                        PartyOption(
                            name = candidate.child("party_name").getValue(String::class.java) ?: "",
                            leaderFaceRes = R.drawable.default_leader,
                            abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                            logoRes = R.drawable.default_logo
                        )
                    }
                }
        }
    }

    // Fetch provincial candidates based on selected province
    LaunchedEffect(selectedProvince) {
        if (selectedProvince != null) {
            database.child("ballots/provincial_legislature/${selectedProvince}/candidates")
                .get().addOnSuccessListener { dataSnapshot ->
                    provincialOptions = dataSnapshot.children.map { candidate ->
                        PartyOption(
                            name = candidate.child("party_name").getValue(String::class.java) ?: "",
                            leaderFaceRes = R.drawable.default_leader,
                            abbreviation = candidate.child("party_acronym").getValue(String::class.java) ?: "",
                            logoRes = R.drawable.default_logo
                        )
                    }
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
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            nationalOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name)
                    },
                    onClick = {
                        selectedNational = option // Set selected National PartyOption
                        expandedNationalVote = false // Close the dropdown
                    }
                )
            }
        }

        // Regional Vote Dropdown
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

        DropdownMenu(
            expanded = expandedRegional,
            onDismissRequest = { expandedRegional = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            regionalOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name)
                    },
                    onClick = {
                        selectedRegional = option // Set selected Regional PartyOption
                        expandedRegional = false // Close the dropdown
                    }
                )
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
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            provincialOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name)
                    },
                    onClick = {
                        selectedProvincial = option // Set selected Provincial PartyOption
                        expandedProvincial = false // Close the dropdown
                    }
                )
            }
        }

        // Display selected votes
        Text(text = "Selected National: ${selectedNational?.name ?: "None"}")
        Text(text = "Selected Regional: ${selectedRegional?.name ?: "None"}")
        Text(text = "Selected Provincial: ${selectedProvincial?.name ?: "None"}")

        Button(
            onClick = {
                castVote(
                    nationalVote = selectedNational,
                    regionalVote = selectedRegional,
                    provincialVote = selectedProvincial,
                    onVoteSuccess = {
                        // Handle successful vote storage (e.g., show a success message)

                        // Optionally clear selections
                        selectedNational = null
                        selectedRegional = null
                        selectedProvincial = null
                    },
                    onVoteError = { exception ->
                        // Handle error (e.g., show an error message)

                    }
                )
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

data class PartyOption(
    val name: String,                  // The name of the party
    val leaderFaceRes: Int,           // Resource ID for the leader's face image
    val abbreviation: String,          // The party's abbreviation
    val logoRes: Int                   // Resource ID for the party logo image
)

data class Candidate(
    val party_name: String? = null,
    val party_acronym: String? = null,
    val party_leader_image_url: String? = null,
    val party_logo_url: String? = null,
    val type: String? = null,  // "party" or "independent"
    val name: String? = null,  // For independent candidates
    val image_url: String? = null,  // For independent candidates
    val createdAt: String? = null
)

data class Region(
    val candidates: Map<String, Candidate>? = null
)

data class Province(
    val candidates: Map<String, Candidate>? = null
)

data class Ballots(
    val national_compensatory: Map<String, Candidate>? = null,
    val national_regional: Map<String, Region>? = null,
    val provincial_legislature: Map<String, Province>? = null
)


fun castVote(
    nationalVote: PartyOption?,
    regionalVote: PartyOption?,
    provincialVote: PartyOption?,
    onVoteSuccess: () -> Unit, // Callback for success
    onVoteError: (Exception) -> Unit // Callback for error
) {
    val voteId = UUID.randomUUID().toString()

    // Structure the vote
    val voteData = mutableMapOf<String, Any?>().apply {
        nationalVote?.let { put("nationalVote", it.abbreviation) } // Assuming abbreviation is used as identifier
        regionalVote?.let { put("regionalVote", it.abbreviation) }
        provincialVote?.let { put("provincialVote", it.abbreviation) }
        put("timestamp", System.currentTimeMillis())
    }

    // Store the vote anonymously in Firebase
    FirebaseDatabase.getInstance().reference
        .child("votes")
        .child(voteId) // Using the generated voteId to ensure anonymity
        .setValue(voteData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onVoteSuccess() // Call success callback
            } else {
                onVoteError(task.exception ?: Exception("Vote casting failed")) // Call error callback with exception
            }
        }
}

