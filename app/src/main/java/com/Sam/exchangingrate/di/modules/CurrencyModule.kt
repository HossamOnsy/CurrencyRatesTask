package com.Sam.exchangingrate.di.modules

import com.Sam.exchangingrate.network.RestApi
import com.Sam.exchangingrate.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable


@Module
object CurrencyModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCurrencyRepository(restApi: RestApi): CurrencyRepository {
        return CurrencyRepository(restApi)
    }

}