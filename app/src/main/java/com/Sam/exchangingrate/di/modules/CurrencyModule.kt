package com.Sam.exchangingrate.di.modules

import com.Sam.exchangingrate.network.RestApi
import com.Sam.exchangingrate.presenters.PresenterImpl
import com.Sam.exchangingrate.repository.CurrencyRepository
import com.Sam.exchangingrate.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
object CurrencyModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun providePresenter(repository: CurrencyRepository): PresenterImpl {
        return PresenterImpl(repository)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCurrencyRepository(restApi: RestApi): CurrencyRepository {
        return CurrencyRepository(restApi)
    }

}