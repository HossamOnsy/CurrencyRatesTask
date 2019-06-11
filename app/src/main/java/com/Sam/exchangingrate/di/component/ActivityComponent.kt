package com.Sam.exchangingrate.di.component

import com.Sam.exchangingrate.di.modules.CurrencyModule
import com.Sam.exchangingrate.di.modules.NetworkModule
import com.Sam.exchangingrate.ui.MainActivity
import com.Sam.exchangingrate.viewmodels.CurrencyConverterViewModel
import dagger.Component


@Component(modules = [(NetworkModule::class),(CurrencyModule::class)])
interface ViewModelComponent {

    fun inject(currencyConverterViewModel: CurrencyConverterViewModel)

}