package com.Sam.exchangingrate.presenters

import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import com.Sam.exchangingrate.models.SingletonVariable.currencyRateMapping
import com.Sam.exchangingrate.repository.CurrencyRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PresenterImpl (var currencyRepository: CurrencyRepository){

    lateinit var  mainPresenter: Presenter
    private var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    init {
        subscription = CompositeDisposable()
    }


    fun fetchCurrencyRates(firstCall: Boolean){
        subscription =
        Observable.interval(1, TimeUnit.SECONDS)
            .flatMap {
            currencyRepository.getCurrencyChanges()
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveDataStart() }
            .doOnTerminate { onRetrieveDataFinish() }
            .subscribe(
                { currencyRateResponseModel ->

                        onRetrieveDataSuccess(currencyRateResponseModel, firstCall)

                },
                { error -> onRetrieveDataError(error) }
            )
    }
    var i = 0;

    private fun onRetrieveDataSuccess(currencyRateResponseModel: CurrencyRatesResponseModel?, firstCall: Boolean) {

        if(i==0) {
            i++;
            mainPresenter.dataFetchedSuccessfully(currencyRateResponseModel);
        }
        else{
            mainPresenter.dataFetchedSuccessfully(null);
        }
        if (currencyRateResponseModel != null) {
            currencyRateMapping = currencyRateResponseModel.rates!!
        }
    }

    private fun onRetrieveDataError(error: Throwable?) {

        Timber.tag("ViewModel")
        loadingVisibility.value = View.GONE
        if (!error?.message.equals("commit already called"))
            Timber.v("Page Number %s ", error?.message)
//            errorMessage.value = R.string.error

    }


    private fun onRetrieveDataFinish() {

        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveDataStart() {
        errorMessage.value = null
        loadingVisibility.value = View.VISIBLE
    }


}