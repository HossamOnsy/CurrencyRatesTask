package com.Sam.exchangingrate.di.component

import com.Sam.exchangingrate.di.modules.CurrencyModule
import com.Sam.exchangingrate.ui.MainActivity
import com.sam.cars.di.modules.NetworkModule
import dagger.Component


@Component(modules = [(NetworkModule::class),(CurrencyModule::class)])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

}