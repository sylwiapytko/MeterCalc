package com.example.metercalc.viewmodel


import androidx.lifecycle.ViewModel
import com.example.metercalc.data.models.MeterReading
import com.example.metercalc.data.models.MeterType
import java.math.BigDecimal
import java.time.LocalDate

class MeterViewModel : ViewModel() {
    //data will be be persisted across UI lifecycles
    //ViewModel survives configuration changes, so data stays available even after rotation.

    private val readings = mutableListOf<MeterReading>() // No need for HomeReading class

    fun addReading(meterType: MeterType, reading: BigDecimal, date: LocalDate) {
        readings.add(MeterReading(meterType, date, reading))
    }


    fun getAllReadings(): List<MeterReading> {
        return readings
    }

    /**
     * Calculates the difference between the two most recent readings for a given meter type.
     * @param meterType The type of meter to calculate the difference for
     * @return The difference between readings, or null if there are less than 2 readings
     */
    fun calculateReadingDifference(meterType: MeterType): BigDecimal? {
        // Filter readings for the specified meter type and sort by date in descending order
        val relevantReadings = readings
            .filter { it.meterType == meterType }
            .sortedByDescending { it.readingDate }

        // Check if we have at least 2 readings
        if (relevantReadings.size < 2) {
            return null
        }

        // Calculate difference between the two most recent readings
        return relevantReadings[0].reading - relevantReadings[1].reading
    }

    /**
     * Gets the two most recent readings for a given meter type.
     * @param meterType The type of meter to get readings for
     * @return A pair of the most recent and previous reading, or null if there are less than 2 readings
     */
    fun getLatestReadings(meterType: MeterType): Pair<MeterReading, MeterReading>? {
        val relevantReadings = readings
            .filter { it.meterType == meterType }
            .sortedByDescending { it.readingDate }

        if (relevantReadings.size < 2) {
            return null
        }

        return Pair(relevantReadings[0], relevantReadings[1])
    }
}