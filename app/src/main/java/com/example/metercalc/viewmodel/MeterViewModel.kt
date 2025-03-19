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
}