package com.example.metercalc.data.models

import java.math.BigDecimal
import java.time.LocalDate

data class MeterReading(
    val meterType: MeterType,
    val readingDate: LocalDate,   //Cant I have it once per all readings?
    val reading: BigDecimal,
    val consumption: BigDecimal,
    val tariffRateUsed: BigDecimal, // The historical rate
    val cost: BigDecimal

    //TODO: maybe add for what months it was?
)