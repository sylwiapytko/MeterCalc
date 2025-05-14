package com.example.metercalc.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metercalc.data.models.MeterType
import com.example.metercalc.data.models.TariffRates
import com.example.metercalc.viewmodel.MeterViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Composable
fun MeterInputScreen(
    viewModel: MeterViewModel = viewModel(),
    onNavigateToReadingsTable: () -> Unit,
    onNavigateToTotalCosts: () -> Unit
) {
    var selectedMeterType by remember { mutableStateOf(MeterType.ELECTRICITY) }
    var readingValue by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enter Meter Reading", style = MaterialTheme.typography.h5)

        }
        Row {
            Button(
                onClick = onNavigateToTotalCosts,
                modifier = Modifier.padding(top = 25.dp, end = 25.dp)
            ) {
                Text("Total Costs")
            }
            Button(onClick = onNavigateToReadingsTable,
                modifier = Modifier.padding(top = 25.dp )                        ) {
                Text("All Readings")
            }
        }

        // Dropdown for Meter Type Selection
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedMeterType.name.replace("_", " "))  // Display formatted name
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                MeterType.values().forEach { meter ->
                    DropdownMenuItem(onClick = {
                        selectedMeterType = meter
                        expanded = false
                        errorMessage = null // Clear error when changing meter type
                    }) {
                        Text(meter.name.replace("_", " "))  // Show formatted name
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Previous Reading Card
        val latestReadings = viewModel.getLatestReadings(selectedMeterType)
        if (latestReadings != null) {
            val (newest, _) = latestReadings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Previous Reading",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text("Value: ${newest.reading}")
                    Text("Date: ${newest.readingDate}")
                }
            }
        }

        // Input Field for Meter Reading
        OutlinedTextField(
            value = readingValue,
            onValueChange = { 
                readingValue = it
                errorMessage = null // Clear error when typing
            },
            label = { Text("New Meter Reading") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker Button
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                if (latestReadings != null && newDate.isBefore(latestReadings.first.readingDate)) {
                    errorMessage = "Date must be later than the previous reading date"
                } else {
                    selectedDate = newDate
                    errorMessage = null
                }
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )

        Button(onClick = { datePicker.show() }) {
            Text("Select Date: $selectedDate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                val value = readingValue.toBigDecimalOrNull()
                if (value == null) {
                    errorMessage = "Enter a valid number"
                    return@Button
                }

                // Validate reading value
                if (latestReadings != null && value <= latestReadings.first.reading) {
                    errorMessage = "New reading must be greater than the previous reading"
                    return@Button
                }

                // Validate date
                if (latestReadings != null && selectedDate.isBefore(latestReadings.first.readingDate)) {
                    errorMessage = "Date must be later than the previous reading date"
                    return@Button
                }

                viewModel.addReading(selectedMeterType, selectedDate, value)
                Toast.makeText(context, "Reading saved!", Toast.LENGTH_SHORT).show()
                readingValue = "" // Reset input field
                errorMessage = null
            },
            enabled = errorMessage == null
        ) {
            Text("Save Reading")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Display Reading Difference and Cost
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Latest Readings for ${selectedMeterType.name.replace("_", " ")}",
                    style = MaterialTheme.typography.h6
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                if (latestReadings != null) {
                    val (newest, previous) = latestReadings
                    Text("Newest Reading: ${newest.reading} (${newest.readingDate})")
                    Text("Previous Reading: ${previous.reading} (${previous.readingDate})")

                        Text("Difference: ${newest.consumption}")
                        Text("Estimated Cost: ${newest.cost}")

                } else {
                    Text("Not enough readings to calculate difference")
                }
            }
        }
    }
}
