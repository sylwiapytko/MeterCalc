package com.example.metercalc.data.models

import java.math.BigDecimal
import java.time.LocalDate

data class MeterReading(
    val meterType: MeterType,
    val readingDate: LocalDate,   //Cant I have it once per all readings?
    val reading: BigDecimal

    //TODO: maybe add for what months it was?
)