package com.example.metercalc.viewmodel

import androidx.lifecycle.ViewModel
import com.example.metercalc.data.models.MeterReading
import com.example.metercalc.data.models.MeterType
import com.example.metercalc.data.models.TariffRates
import java.math.BigDecimal
import java.time.LocalDate

class MeterViewModel : ViewModel() {
    //data will be be persisted across UI lifecycles
    //ViewModel survives configuration changes, so data stays available even after rotation.

    private val readings = mutableListOf<MeterReading>()

    /**
     * Adds a new meter reading to the list.
     *
     * @param meterType The type of the meter (e.g., GAS, ELECTRICITY).
     * @param readingDate The date the reading was taken.
     * @param readingValue The value of the meter reading.
     */
    fun addReading(
        meterType: MeterType,
        readingDate: LocalDate,
        readingValue: BigDecimal// Optional, defaults to null
    ) {
        var consumption = readingValue - (getLatestReading(meterType)?.reading ?: readingValue)
        var tariffRateUsed = TariffRates.getRate(meterType)
        val newReading = MeterReading(
            meterType = meterType,
            readingDate = readingDate,
            reading = readingValue,
            consumption= consumption,
            tariffRateUsed = tariffRateUsed,
            cost = consumption * tariffRateUsed
        )
        readings.add(newReading)
        // Optional: If you need to re-sort or re-process data immediately after adding
        // readings.sortBy { it.readingDate } // Example: keep readings sorted by date
    }


    fun getAllReadings(): List<MeterReading> {
        return readings.toList() // Return a copy to prevent external modification
    }

    /**
     * Calculates the difference between the two most recent readings for a given meter type.
     * @param meterType The type of meter to calculate the difference for
     * @return The difference between readings, or null if there are less than 2 readings
     */
    fun calculateReadingDifference(meterType: MeterType): BigDecimal {
        // Filter readings for the specified meter type and sort by date in descending order
        val relevantReadings = readings
            .filter { it.meterType == meterType }
            .sortedByDescending { it.readingDate }

        // Check if we have at least 2 readings
        if (relevantReadings.size < 2) {
            return BigDecimal.ZERO
        }

        // Calculate difference between the two most recent readings
        return relevantReadings[0].reading - relevantReadings[1].reading
    }
    /**
     * Gets the single most recent reading for a given meter type.
     *
     * @param meterType The type of meter to get the latest reading for.
     * @return The most recent MeterReading for the specified type, or null if no readings exist for that type.
     */
    fun getLatestReading(meterType: MeterType): MeterReading? {
        return readings
            .filter { it.meterType == meterType } // Filter by the specified meter type
            .maxByOrNull { it.readingDate }      // Find the element with the maximum readingDate
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

        if (relevantReadings.isEmpty()) {
            return null
        }
        // If only one reading, pair it with itself.
        // If two or more, pair the latest two.
        return Pair(relevantReadings[0], relevantReadings.getOrElse(1) { relevantReadings[0] })
    }

    /**
     * Deletes the most recent reading for a given meter type.
     * @param meterType The type of meter to delete the latest reading for
     * @return true if a reading was deleted, false if there were no readings to delete
     */
    fun deleteLatestReading(meterType: MeterType): Boolean {
        val latestReading = getLatestReading(meterType) ?: return false
        return readings.remove(latestReading)
    }
}