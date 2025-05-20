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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

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
        //Get all readings for the selected meter type
        val allReadingsForType = viewModel.getAllReadings().filter { it.meterType == selectedMeterType }
            .sortedByDescending { it.readingDate }
        //Get the latest reading for the selected meter type
        val latestReading = allReadingsForType.firstOrNull()

        // Date Picker Button
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                if (latestReading != null && newDate.isBefore(latestReading.readingDate)) {
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
                if (latestReading != null && value <= latestReading.reading) {
                    errorMessage = "New reading must be greater than the previous reading"
                    return@Button
                }

                // Validate date
                if (latestReading != null && selectedDate.isBefore(latestReading.readingDate)) {
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

        // Previous Readings Section

        if (allReadingsForType.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Take remaining space
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        "Previous Readings",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allReadingsForType) { reading ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Date: ${reading.readingDate}")
                                    Text("Reading: ${reading.reading}")
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Difference: ${reading.consumption}")
                                    Text("Cost: ${reading.cost}")
                                }
                            }
                            
                            if (reading != allReadingsForType.last()) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
