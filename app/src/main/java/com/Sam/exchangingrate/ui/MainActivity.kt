package com.Sam.exchangingrate.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.viewmodels.CurrencyConverterViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import androidx.lifecycle.ViewModelProviders




class MainActivity : AppCompatActivity(), CurrencyConverterRecyclerAdapter.CurrenyConverterAdapterListener {




    lateinit var mainCurrencyConverterViewModel: CurrencyConverterViewModel
    lateinit var currencyRatesAdapter: CurrencyConverterRecyclerAdapter
    var arrayList = ArrayList<CurrencyRatesRecyclerObject>()
    lateinit var toast :Toast
    var initialValueEntered = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.Sam.exchangingrate.R.layout.activity_main)
        mainCurrencyConverterViewModel = ViewModelProviders.of(this).get(CurrencyConverterViewModel::class.java!!)


        recyclerInitation()
        recyclerScrollListener()
        setObservers()

        mainCurrencyConverterViewModel.fetchCurrencyRates()
        toast = Toast.makeText(applicationContext, "Error Occured",  Toast.LENGTH_SHORT)

    }

    private fun setObservers() {
        latestCurrencyConversionObserve()
        valueEnteredObserve()
        loadingObserve()
        successObserve()
        errorObserve()
    }

    private fun latestCurrencyConversionObserve() {
        mainCurrencyConverterViewModel.currencyRateMapping.observe(this, Observer { it->
            if(it!=null){
                currencyRatesAdapter.currencyLatestDataConversion(it)
            }
        })

    }

    private fun valueEnteredObserve() {

        mainCurrencyConverterViewModel.valueEntered.observe(this, Observer { it->
                if(it!=null){
                    if(!mainCurrencyConverterViewModel.isScrolling)
                        notifyItemRangeChanged()
                }
        })

    }

    private fun loadingObserve() {
        mainCurrencyConverterViewModel.loadingVisibility.observe(this, Observer { it->
            progress_bar.visibility = it

        })

    }

    private fun errorObserve() {
        mainCurrencyConverterViewModel.errorMessage.observe(this, Observer { it->
            if(it!=null){

                if(toast!=null)
                    toast.cancel();

                toast = Toast.makeText(applicationContext, it,  Toast.LENGTH_SHORT)
                toast.show()
            }
            else{

                if(toast!=null)
                    toast.cancel()
            }

        })

    }

    private fun successObserve() {
        mainCurrencyConverterViewModel.currencyRateResponseMLD.observe(this, Observer { it->
            if (it != null) {

                arrayList.add(CurrencyRatesRecyclerObject("EUR", "0"))
                for( i in it.rates!!.toList()){
                    arrayList.add(CurrencyRatesRecyclerObject(i.first, i.second.toString()))
                }

                currencyRatesAdapter.updateList(arrayList)
            }
            else if(!mainCurrencyConverterViewModel.isScrolling&&!mainCurrencyConverterViewModel.isUpdatingText){

                currencyRatesAdapter.notifyItemRangeChanged(1,arrayList.size)
            }
        })
    }


    private fun notifyItemRangeChanged() {
        currencyRatesAdapter.valueAltered(mainCurrencyConverterViewModel.valueEntered.value,arrayList)
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
            val viewManager = LinearLayoutManager(this@MainActivity)
            val arrayList = ArrayList<CurrencyRatesRecyclerObject>()
            layoutManager = viewManager
            setUpRecyclerAdapter(arrayList)
        }
    }

    fun setUpRecyclerAdapter(valueList: ArrayList<CurrencyRatesRecyclerObject>) {
        recycler_view.adapter = null
        currencyRatesAdapter = CurrencyConverterRecyclerAdapter(this@MainActivity, valueList,this,initialValueEntered)
        recycler_view.setItemAnimator(SlideInUpAnimator())
        recycler_view.adapter = currencyRatesAdapter
    }

    fun receiveNewList(valueList: ArrayList<CurrencyRatesRecyclerObject>) {
        if(mainCurrencyConverterViewModel.valueEntered.value!=null)
            initialValueEntered = mainCurrencyConverterViewModel.valueEntered.value!!


        setUpRecyclerAdapter(valueList)
    }


    override fun isUpdatingText(textIsChanging: Boolean) {
        mainCurrencyConverterViewModel.isUpdatingText  = textIsChanging
    }


    override fun valueChanged(valueEntered: Double) {
        mainCurrencyConverterViewModel.valueEntered.value = valueEntered

    }



}
