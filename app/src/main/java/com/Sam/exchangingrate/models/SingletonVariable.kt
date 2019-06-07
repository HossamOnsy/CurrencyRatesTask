package com.Sam.exchangingrate.models

import androidx.lifecycle.MutableLiveData

object SingletonVariable {
    var valueEntered = MutableLiveData<Int>()
    init {
        valueEntered.value = 0
    }
    lateinit var currencyRateMapping : Map<String, Double> ;
}