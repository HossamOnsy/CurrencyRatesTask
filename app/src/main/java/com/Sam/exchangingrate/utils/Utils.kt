package com.Sam.exchangingrate.utils

import com.Sam.exchangingrate.R


// Made is as a Util Function so later on it would used whenever an Exchanging Rate is required
// Of course we can change currencyRateMapping -> fetch it from Room or the last saved CurrencyConversionList

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

    fun getFlagOfCurrencyHost(currency: String): Int {
        return when(currency) {
            "AUD" -> R.drawable.ic_aud
            "BGN" -> R.drawable.ic_bgn
            "BRL" -> R.drawable.ic_brl
            "CAD" -> R.drawable.ic_cad
            "CHF" -> R.drawable.ic_chf
            "CNY" -> R.drawable.ic_cny
            "CZK" -> R.drawable.ic_czk
            "DKK" -> R.drawable.ic_dkk
            "EUR" -> R.drawable.ic_eur
            "GBP" -> R.drawable.ic_gbp
            "HKD" -> R.drawable.ic_hkd
            "HRK" -> R.drawable.ic_hrk
            "HUF" -> R.drawable.ic_huf
            "IDR" -> R.drawable.ic_idr
            "ILS" -> R.drawable.ic_ils
            "INR" -> R.drawable.ic_inr
            "ISK" -> R.drawable.ic_isk
            "JPY" -> R.drawable.ic_jpy
            "KRW" -> R.drawable.ic_krw
            "MXN" -> R.drawable.ic_mxn
            "MYR" -> R.drawable.ic_myr
            "NOK" -> R.drawable.ic_nok
            "NZD" -> R.drawable.ic_nzd
            "PHP" -> R.drawable.ic_php
            "PLN" -> R.drawable.ic_pln
            "RON" -> R.drawable.ic_ron
            "RUB" -> R.drawable.ic_rub
            "SEK" -> R.drawable.ic_sek
            "SGD" -> R.drawable.ic_sgd
            "THB" -> R.drawable.ic_thb
            "TRY" -> R.drawable.ic_try
            "USD" -> R.drawable.ic_usd
            "ZAR" -> R.drawable.ic_zar
            else -> R.drawable.ic_eur
        }
    }

    fun getNameOfCurrency(currency: String): String {
        return when(currency) {
            "AUD" -> "Australian Dollar"
            "BGN" -> "Bulgarian Lev"
            "BRL" -> "Brazilian Real"
            "CAD" -> "Canadian Dollar"
            "CHF" -> "Swiss Franc"
            "CNY" -> "Chinese Yuan"
            "CZK" -> "Czech Koruna"
            "DKK" -> "Danish Krone"
            "GBP" -> "British Pound"
            "HKD" -> "Hong Kong Dollar"
            "HRK" -> "Croatian Kuna"
            "HUF" -> "Hungarian Forint"
            "IDR" -> "Indonesian Rupiah"
            "ILS" -> "Israeli New Shekel"
            "INR" -> "Indian Rupee"
            "ISK" -> "Icelandic Króna"
            "JPY" -> "Japanese Yen"
            "KRW" -> "South Korean won"
            "MXN" -> "Mexican Peso"
            "MYR" -> "Malaysian Ringgit"
            "NOK" -> "Norwegian Krone"
            "NZD" -> "New Zealand Dollar"
            "PHP" -> "Philippine Piso"
            "PLN" -> "Poland Zloty"
            "RON" -> "Romania Leu"
            "RUB" -> "Russian Rouble"
            "SEK" -> "Swedish Krona"
            "SGD" -> "Singapore Dollar"
            "THB" -> "Thai Baht"
            "TRY" -> "Turkish lira"
            "USD" -> "United States Dollar"
            "ZAR" -> "South African Rand"
            "EUR" -> "Euro"
            else -> ""
        }
    }
   
}