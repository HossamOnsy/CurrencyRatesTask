package com.Sam.exchangingrate.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.viewmodels.CurrencyConverterViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.lottie_animation
import kotlinx.android.synthetic.main.activity_splash.*


class MainActivity : AppCompatActivity(), CurrencyConverterRecyclerAdapter.CurrenyConverterAdapterListener {

    lateinit var mainCurrencyConverterViewModel: CurrencyConverterViewModel
    lateinit var currencyRatesAdapter: CurrencyConverterRecyclerAdapter
    lateinit var toast: Toast
    var arrayList = ArrayList<CurrencyRatesRecyclerObject>()
    var currentValue = 0.0
    lateinit var viewManager : LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.Sam.exchangingrate.R.layout.activity_main)

        initateData()

        recyclerInitation()
        recyclerScrollListener()
        setObservers()
        startRequest()

    }

    private fun startRequest() {
        mainCurrencyConverterViewModel.fetchCurrencyRates()
    }

    @SuppressLint("ShowToast")
    private fun initateData() {
        mainCurrencyConverterViewModel = ViewModelProviders.of(this).get(CurrencyConverterViewModel::class.java)
        lottie_animation.setAnimation("currency.json")
        toast = Toast.makeText(applicationContext, "Error Occured , please try again later ...", Toast.LENGTH_SHORT)
    }

    private fun setObservers() {
        latestCurrencyConversionObserve()
        valueEnteredObserve()
        loadingObserve()
        successObserve()
        errorObserve()
    }

    private fun latestCurrencyConversionObserve() {
        mainCurrencyConverterViewModel.currencyRateMapping.observe(this, Observer {
            if (it != null) {
                currencyRatesAdapter.currencyLatestDataConversion(it)
            }
        })
    }

    private fun valueEnteredObserve() {

        mainCurrencyConverterViewModel.valueEntered.observe(this, Observer {
            if (it != null) {
                if (recycler_view.scrollState == RecyclerView.SCROLL_STATE_IDLE && !viewManager.isSmoothScrolling)

                        recycler_view.post {
                                currencyRatesAdapter.valueAltered(mainCurrencyConverterViewModel.valueEntered.value, arrayList)

                        };

            }
        })

    }

    private fun loadingObserve() {
        mainCurrencyConverterViewModel.loadingVisibility.observe(this, Observer {

            lottie_animation.visibility = it

            if(it == View.VISIBLE){
                    lottie_animation.playAnimation()
            }else{
                lottie_animation.cancelAnimation()
            }
        })

    }

    private fun errorObserve() {
        mainCurrencyConverterViewModel.errorMessage.observe(this, Observer {
            toast.cancel()
            if (it != null) {
                toast = Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
                toast.show()
            }
        })

    }

    private fun successObserve() {
        mainCurrencyConverterViewModel.currencyRateResponseMLD.observe(this, Observer {
            if (it != null) {

                arrayList.add(CurrencyRatesRecyclerObject("EUR", "0"))
                for (i in it.rates!!.toList()) {
                    arrayList.add(CurrencyRatesRecyclerObject(i.first, i.second.toString()))
                }

                currencyRatesAdapter.updateList(arrayList)
            } else if (!mainCurrencyConverterViewModel.isScrolling && !mainCurrencyConverterViewModel.isUpdatingText) {

                currencyRatesAdapter.notifyItemRangeChanged(1, arrayList.size)
            }
        })
    }

    private fun recyclerScrollListener() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mainCurrencyConverterViewModel.isScrolling = recycler_view.scrollState != RecyclerView.SCROLL_STATE_IDLE
            }
        })
    }

    private fun recyclerInitation() {
        recycler_view.apply {
            setHasFixedSize(true)
            viewManager = LinearLayoutManager(this@MainActivity)
            val arrayList = ArrayList<CurrencyRatesRecyclerObject>()
            layoutManager = viewManager
            setUpRecyclerAdapter(arrayList)
        }
    }

    fun setUpRecyclerAdapter(currenciesList: ArrayList<CurrencyRatesRecyclerObject>) {
        recycler_view.adapter = null
        currencyRatesAdapter = CurrencyConverterRecyclerAdapter(
            currenciesList = currenciesList,
            currencyConverterAdapterListener = this,
            currentValue = currentValue
        )
        (recycler_view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recycler_view.adapter = currencyRatesAdapter
    }

    override fun listAltered() {
        viewManager.smoothScrollToPosition(recycler_view, RecyclerView.State(),0)
    }

    override fun isUpdatingText(textIsChanging: Boolean) {
        mainCurrencyConverterViewModel.isUpdatingText = textIsChanging
    }

    override fun valueChanged(valueEntered: Double) {

        mainCurrencyConverterViewModel.valueEntered.value = valueEntered
    }


}
