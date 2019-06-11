package com.Sam.exchangingrate.utils


object AppUtils {
    fun getValueAfterConversion(
        value: Double,
        currencyRateMapping: Map<String, Double>?,
        second: String
    ): Double {
        var result = 0.0
        if (currencyRateMapping != null && currencyRateMapping.get(second) != null)
            result = value * currencyRateMapping.get(second)!!

        return result
    }

//    fun getResultAfterConversion(second: String, value: Double): Double {
//        var result = 0.0
//        if (currencyRateMapping != null && currencyRateMapping!!.get(second) != null)
//            result = value * currencyRateMapping!!.get(second)!!
//
//        return result
//    }
}