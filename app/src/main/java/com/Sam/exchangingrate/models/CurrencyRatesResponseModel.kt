package com.Sam.exchangingrate.models

import com.squareup.moshi.Json


class CurrencyRatesResponseModel {

    @Json(name = "base")
    var base: String? = null
    @Json(name = "date")
    var date: String? = null
    @Json(name = "rates")
    var rates: Map<String,Double>? = null

}