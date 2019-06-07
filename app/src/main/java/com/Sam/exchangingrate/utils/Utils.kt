package com.Sam.exchangingrate.utils

import com.Sam.exchangingrate.models.SingletonVariable.currencyRateMapping
import com.Sam.exchangingrate.models.SingletonVariable.valueEntered

object AppUtils {
    fun getValueAfterConversion(second: String): Double {
        var result = 0.0
        if (valueEntered.value != null && currencyRateMapping.get(second) != null)
            result = valueEntered.value!! * currencyRateMapping.get(second)!!
        return result
    }
}