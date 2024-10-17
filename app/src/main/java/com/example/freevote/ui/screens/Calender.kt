package com.example.freevote.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calender()
        }
    }
}

@Composable
fun Calender() {
    var selectedDateString by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Define South African holidays
    val holidays = mapOf(
        "01/01" to "New Year's Day",
        "21/03" to "Human Rights Day",
        "27/04" to "Freedom Day",
        "01/05" to "Workers' Day",
        "16/06" to "Youth Day",
        "09/08" to "Women's Day",
        "24/09" to "Heritage Day",
        "16/12" to "Day of Reconciliation",
        "25/12" to "Christmas Day",
        "26/12" to "Day of Goodwill"
    )

    Column(modifier = Modifier.padding(top = 100.dp).background(Color.White)) {
        // Button to open DatePickerDialog
        Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Select Date using DatePickerDialog")
        }

        // Display the selected date from DatePickerDialog
        if (selectedDateString.isNotEmpty()) {
            Text(text = "Selected Date: $selectedDateString")
        }

        // Show DatePickerDialog when the button is clicked
        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                LocalContext.current,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val selectedDate = calendar.time
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    selectedDateString = sdf.format(selectedDate) // Format the date
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // CalendarView for date selection
        AndroidView(factory = { CalendarView(it) }, modifier = Modifier.fillMaxWidth()) { calendarView ->
            // Highlight holidays
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
                val dayString = sdf.format(selectedDate)

                // Display holiday message if the selected date is a holiday
                holidays[dayString]?.let { holidayName ->
                    // Use the context from the AndroidView
                    Toast.makeText(calendarView.context, "Holiday: $holidayName", Toast.LENGTH_SHORT).show()
                }

                // Update selected date string
                selectedDateString = sdf.format(selectedDate) // Format the date
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Calender()
}
