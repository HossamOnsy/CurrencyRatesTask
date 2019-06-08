package com.Sam.exchangingrate.repository


import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import com.Sam.exchangingrate.network.RestApi
import com.Sam.exchangingrate.utils.currencyKey
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CurrencyRepository (var restApi: RestApi){

    fun getCurrencyChanges(): Observable<CurrencyRatesResponseModel> {
        return restApi.getCurrencyRates(currencyKey)
            .debounce(400, TimeUnit.MILLISECONDS)
            .map {
                Timber.d("Mapping CurrencyRates to UIData... ${currencyKey}")
                it
            }
            .onErrorReturn { CurrencyRatesResponseModel() }
    }
}
