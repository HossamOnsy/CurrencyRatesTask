package com.Sam.exchangingrate.utils

import com.Sam.exchangingrate.models.ObservableObject
import com.Sam.exchangingrate.models.SingletonVariable.currencyRateMapping

object AppUtils {
    fun getValueAfterConversion(second: String): Double {
        var result = 0.0
        if (ObservableObject.valueEntered != null &&currencyRateMapping!=null&& currencyRateMapping!!.get(second) != null)
            result = ObservableObject.valueEntered!! * currencyRateMapping!!.get(second)!!

        return result.toDouble()
    }

    fun getResultAfterConversion(second: String, value: Double): Double {
        var result = 0.0
        if (currencyRateMapping!=null&& currencyRateMapping!!.get(second) != null)
            result = value * currencyRateMapping!!.get(second)!!

        return result.toDouble()
    }
}