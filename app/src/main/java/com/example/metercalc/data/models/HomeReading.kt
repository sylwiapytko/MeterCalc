//package com.example.metercalc.data.models
//
//import java.math.BigDecimal
//import java.time.LocalDate
//
//class HomeReading {
//    //TODO: to be deleted, as ViewModel should have been used to persist data in UI lifecycle.
//    private val readings = mutableListOf<MeterReading>()
//
//    fun addReading(meterType: MeterType, reading: BigDecimal) {
//        readings.add(MeterReading(meterType, LocalDate.now(), reading)) // allow to input readingDate
//    }
//
//    fun getReadings(): List<MeterReading> {
//        return readings
//    }
//}