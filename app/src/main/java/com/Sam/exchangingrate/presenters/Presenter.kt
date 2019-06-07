package com.Sam.exchangingrate.presenters

import com.Sam.exchangingrate.models.CurrencyRatesResponseModel

interface Presenter {
     fun dataFetchedSuccessfully(currencyRateResponseModel: CurrencyRatesResponseModel?)
}