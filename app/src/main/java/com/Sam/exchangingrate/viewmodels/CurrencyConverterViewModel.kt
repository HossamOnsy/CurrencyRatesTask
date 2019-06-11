package com.Sam.exchangingrate.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.Sam.exchangingrate.base.BaseViewModel
import com.Sam.exchangingrate.di.component.DaggerViewModelComponent
import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import com.Sam.exchangingrate.repository.CurrencyRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyConverterViewModel : BaseViewModel() {


    @Inject
    lateinit var currencyRepository: CurrencyRepository

    private var subscription: Disposable
    val currencyRateMapping: MutableLiveData<Map<String, Double>> = MutableLiveData()
    val currencyRateResponseMLD: MutableLiveData<CurrencyRatesResponseModel> = MutableLiveData()
    val valueEntered: MutableLiveData<Double> = MutableLiveData()

    var isScrolling: Boolean = false
    var isUpdatingText: Boolean = false
    var isFirstCall: Boolean = true

    init {
        subscription = CompositeDisposable()
        valueEntered.value =0.0
        DaggerViewModelComponent.builder().build().inject(this)
    }

    fun fetchCurrencyRates() {
        subscription =
            Observable.interval(1, TimeUnit.SECONDS)
                .flatMap {
                    currencyRepository.getCurrencyChanges()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveDataStart() }
                .doOnTerminate{ onRetrieveDataFinish() }
                .subscribe(
                    { currencyRateResponseModel ->

                        onRetrieveDataSuccess(currencyRateResponseModel)

                    },
                    { error -> onRetrieveDataError(error) }
                )
    }

    private fun onRetrieveDataSuccess(currencyRateResponseModel: CurrencyRatesResponseModel?) {

        loadingVisibility.value = View.GONE

        if (isFirstCall) {
            isFirstCall = false
            currencyRateResponseMLD.value = currencyRateResponseModel
        } else if (!isScrolling&&!isUpdatingText){
            currencyRateResponseMLD.value = null
        }

        if (currencyRateResponseModel != null) {
            currencyRateMapping.value = currencyRateResponseModel.rates!!
        }
    }



    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }


}