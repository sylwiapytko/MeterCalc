package com.example.metercalc.data.models

import java.math.BigDecimal

object TariffRates {

    // Default tariff rates using BigDecimal for precision
    private val rates = mutableMapOf(
        MeterType.ELECTRICITY to BigDecimal("0.4346"),
        MeterType.WATER_COLD to BigDecimal("13.72"),
        MeterType.WATER_HOT to BigDecimal("25.0"),
        MeterType.HEATING to BigDecimal("130.0")
    )

    // Function to get the rate for a given meter type
    fun getRate(meterType: MeterType): BigDecimal {
        return rates[meterType] ?: BigDecimal.ZERO
    }

    // Function to allow users to update a tariff rate
    fun updateRate(meterType: MeterType, newRate: BigDecimal) {
        rates[meterType] = newRate
    }

    // Function to return all rates (useful for UI display)
    fun getAllRates(): Map<MeterType, BigDecimal> {
        return rates.toMap() // Returns an immutable copy
    }

}