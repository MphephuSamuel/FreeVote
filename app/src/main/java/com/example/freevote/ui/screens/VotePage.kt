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


val AbhayaLibreExtraBold = FontFamily(
    Font(R.font.abhaya_libre_extrabold)
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VotePage(modifier: Modifier, navController: NavController, viewModel: MainViewModel) {
    // Define the custom font
    val Rubikmoonroocks = FontFamily(
        Font(R.font.rubik_moonrocks)
    )
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
                // Call LocationSelection here with provinces list
                LocationDropdown(provinces)
            }
        }

        // Spacer between sections
        Spacer(
            modifier = Modifier.fillMaxWidth().height(16.dp)
        )

        // Vote section (assuming you will implement VoteDropdownMenu similarly)
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
                // Assuming you have a similar VoteDropdownMenu composable
                VoteDropdownMenu()
            }
        }
    }
}
@Composable
fun LocationDropdown(provinces: List<String>) {
    // State variables for managing dropdown visibility and selected items
    var expandedProvince by remember { mutableStateOf(false) }
    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var locationConfirmation by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Province Dropdown
        Button(
            onClick = { expandedProvince = !expandedProvince },
            modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, Color.Black)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = selectedProvince ?: "Select Province")
        }

        DropdownMenu(
            expanded = expandedProvince,
            onDismissRequest = { expandedProvince = false },
            modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
        ) {
            provinces.forEach { province ->
                DropdownMenuItem(
                    text = {
                        Text(province)
                    },
                    onClick = {
                        selectedProvince = province
                        expandedProvince = false // Close the dropdown after selection
                    }
                )
            }
        }

        // Confirm button for selected province
        if (selectedProvince != null) {
            Button(
                onClick = { locationConfirmation = true },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
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
                            selectedProvince = null
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            locationConfirmation = false
                            selectedProvince = null
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
fun VoteDropdownMenu() {
    // State variables for each dropdown
    var expandedNationalVote by remember { mutableStateOf(false) }
    var expandedNational by remember { mutableStateOf(false) }
    var expandedRegional by remember { mutableStateOf(false) }
    var expandedProvincial by remember { mutableStateOf(false) }

    // State variables to hold the selected values
    var selectedNational by remember { mutableStateOf<String?>(null) }
    var selectedRegional by remember { mutableStateOf<String?>(null) }
    var selectedProvincial by remember { mutableStateOf<String?>(null) }

    // State variables to show confirmation dialogs
    var showNationalConfirmation by remember { mutableStateOf(false) }

    // Sample data for National, Regional, Provincial, Ward Councillor, and PR Councillor votes
    //val nationalOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")
    val regionalOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")
    val provincialOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")

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
            Text(text = "National ")
        }

        DropdownMenu(
            expanded = expandedNationalVote,
            onDismissRequest = { expandedNationalVote = false },
            modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Column {
                        Text("National", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedNational ?: "")
                    }
                },
                onClick = {
                    expandedNational = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Column {
                        Text("Regional", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedRegional ?: "", )
                    }
                },
                onClick = {
                    expandedRegional = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Column {
                        Text("Provincial", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedProvincial ?: "")
                    }
                },
                onClick = {
                    expandedProvincial = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
        }
        data class PartyOption(
            val name: String,
            val leaderFaceRes: Int,  // Use Int for resource ID
            val abbreviation: String,
            val logoRes: Int         // Use Int for resource ID
        )
        val nationalOptions = listOf(
            PartyOption("Economic Freedom Fighters", R.drawable.malema, "EFF", R.drawable.eff),
            PartyOption("African National Congress", R.drawable.ramaphosa, "ANC", R.drawable.anc),
            PartyOption("uMkhonto Wesizwe", R.drawable.zuma, "MK", R.drawable.mk)
        )

        // National Vote Options
        DropdownMenu(
            expanded = expandedNational,
            onDismissRequest = { expandedNational = false },
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
            nationalOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Leader face image from resources
                            Image(
                                painter = painterResource(option.leaderFaceRes),
                                contentDescription = "Leader Face",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
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
                            // Party logo image from resources
                            Image(
                                painter = painterResource(option.logoRes),
                                contentDescription = "Party Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                            CrossCheckbox(
                                checked = option.name == selectedNational,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedNational = option.name
                                        expandedNational = false
                                        expandedNationalVote = true
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedNational = option.name
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

        // Regional Vote Options
        DropdownMenu(
            expanded = expandedRegional,
            onDismissRequest = { expandedRegional = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xfffcae55), Color.White)
                    )
                )
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            regionalOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedRegional,
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
                        expandedRegional = false
                        expandedNationalVote = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < regionalOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // Provincial Vote Options
        DropdownMenu(
            expanded = expandedProvincial,
            onDismissRequest = { expandedProvincial = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xffdd88ab), Color.White)
                    )
                )
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            provincialOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedProvincial,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedProvincial = option
                                        showNationalConfirmation = true

                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedProvincial = option
                        expandedProvincial = false
                        showNationalConfirmation = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < provincialOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // National Vote Confirmation Dialog
        if (showNationalConfirmation) {
            AlertDialog(
                onDismissRequest = { showNationalConfirmation = false },
                title = { Text("Confirm National Votes") },
                text = {
                    Column {
                        Text("National: ${selectedNational ?: "None"}")
                        Text("Regional: ${selectedRegional ?: "None"}")
                        Text("Provincial: ${selectedProvincial ?: "None"}")
                    }
                },
                confirmButton = {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            showNationalConfirmation = false
                            Toast.makeText(
                                context,
                                "National Votes Submitted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showNationalConfirmation = false
                            selectedNational = null
                            selectedRegional = null
                            selectedProvincial = null
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
