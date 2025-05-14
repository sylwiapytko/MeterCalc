package com.example.metercalc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metercalc.data.models.MeterType
import com.example.metercalc.data.models.TariffRates
import com.example.metercalc.viewmodel.MeterViewModel
import java.math.BigDecimal

@Composable
fun ReadingsTableScreen(
    viewModel: MeterViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val allReadings = viewModel.getAllReadings()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("All Readings", style = MaterialTheme.typography.h5)
            Button(onClick = onNavigateBack,
                modifier = Modifier.padding(top = 25.dp)) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group readings by meter type
        val readingsByType = allReadings.groupBy { it.meterType }

        LazyColumn {
            readingsByType.forEach { (meterType, readings) ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                meterType.name.replace("_", " "),
                                style = MaterialTheme.typography.h6
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))

                            // Sort readings by date in descending order
                            val sortedReadings = readings.sortedByDescending { it.readingDate }

                            // Display readings in a table format
                            sortedReadings.forEachIndexed { index, reading ->
                                if (index < sortedReadings.size - 1) {
//                                    val nextReading = sortedReadings[index + 1]
                                    val difference = reading.consumption
                                    val cost = reading.cost

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
                                            Text("Difference: $difference")
                                            Text("Cost: $cost")
                                        }
                                    }
                                    
                                    if (index < sortedReadings.size - 2) {
                                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 