package com.Sam.exchangingrate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.di.component.DaggerActivityComponent
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import com.Sam.exchangingrate.models.ObservableObject.isScrolling
import com.Sam.exchangingrate.presenters.Presenter
import com.Sam.exchangingrate.presenters.PresenterImpl
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Presenter {

    @Inject
    lateinit var mainPresenterImpl: PresenterImpl
    lateinit var currencyRatesAdapter: DataRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.Sam.exchangingrate.R.layout.activity_main)

        DaggerActivityComponent.builder().build().inject(this)

        recyclerInitation()
        recyclerScrollListener()
        mainPresenterImpl.mainPresenter = this
        mainPresenterImpl.fetchCurrencyRates(true)

    }
    private fun recyclerScrollListener() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(recycler_view.scrollState == RecyclerView.SCROLL_STATE_IDLE ){
                        isScrolling = false
                }else{
                    isScrolling = true
                }
            }

        })
    }
    private fun recyclerInitation() {
        recycler_view.apply {
            setHasFixedSize(true)
            val viewManager = LinearLayoutManager(this@MainActivity)
            var arrayList = ArrayList<CurrencyRatesRecyclerObject>()

            layoutManager = viewManager
            setUpRecyclerAdapter(arrayList)
        }
    }

    fun setUpRecyclerAdapter(valueList: ArrayList<CurrencyRatesRecyclerObject>) {
        recycler_view.adapter = null
        currencyRatesAdapter = DataRecyclerAdapter(this@MainActivity, valueList)
        recycler_view.setItemAnimator(SlideInUpAnimator())
        recycler_view.adapter = currencyRatesAdapter

    }

    var arrayList = ArrayList<CurrencyRatesRecyclerObject>()
    override fun dataFetchedSuccessfully(currencyRateResponseModel: CurrencyRatesResponseModel?) {
        if (currencyRateResponseModel != null) {

            arrayList.add(CurrencyRatesRecyclerObject("EUR", "0"))
            for( i in currencyRateResponseModel.rates!!.toList()){
                arrayList.add(CurrencyRatesRecyclerObject(i.first, i.second.toString()))
            }

            currencyRatesAdapter.updateList(arrayList)

        }
        else {
            if(recycler_view.scrollState == RecyclerView.SCROLL_STATE_IDLE && DataRecyclerAdapter.textChanged){
                currencyRatesAdapter.notifyItemRangeChanged(1,arrayList.size)
            }

        }

    }

    fun receiveNewList(valueList: ArrayList<CurrencyRatesRecyclerObject>) {
        setUpRecyclerAdapter(valueList)

    }


}
