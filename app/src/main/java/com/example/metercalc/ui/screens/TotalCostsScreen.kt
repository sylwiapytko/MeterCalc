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
import java.time.LocalDate

@Composable
fun TotalCostsScreen(
    viewModel: MeterViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val tariffRates = remember { TariffRates() }
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
            Text("Total Costs by Date", style = MaterialTheme.typography.h5)
            Button(onClick = onNavigateBack) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group readings by date and calculate costs
        val readingsByDate = allReadings
            .groupBy { it.readingDate }
            .mapValues { (_, readings) ->
                readings.groupBy { it.meterType }
                    .mapValues { (meterType, meterReadings) ->
                        val sortedReadings = meterReadings.sortedByDescending { it.readingDate }
                        if (sortedReadings.size >= 2) {
                            val difference = sortedReadings[0].reading - sortedReadings[1].reading
                            val rate = tariffRates.getRate(meterType)
                            difference * rate
                        } else BigDecimal.ZERO
                    }
            }
            .toList()
            .sortedByDescending { it.first }

        LazyColumn {
            items(readingsByDate) { (date, costsByMeter) ->
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
                            "Date: $date",
                            style = MaterialTheme.typography.h6
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))

                        // Display costs for each meter type
                        costsByMeter.forEach { (meterType, cost) ->
                            if (cost != BigDecimal.ZERO) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(meterType.name.replace("_", " "))
                                    Text("Cost: $cost")
                                }
                            }
                        }

                        // Calculate and display total cost for the date
                        val totalCost = costsByMeter.values.sumOf { it }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total Cost",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(
                                "Total: $totalCost",
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                    }
                }
            }
        }
    }
} 