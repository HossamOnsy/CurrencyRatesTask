package com.Sam.exchangingrate.network

import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {

    @GET("/latest")
    fun getCurrencyRates(
        @Query("base") currencyName: String
    ): Observable<CurrencyRatesResponseModel>


}