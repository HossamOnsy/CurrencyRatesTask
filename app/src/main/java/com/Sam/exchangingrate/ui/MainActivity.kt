package com.Sam.exchangingrate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.R
import com.Sam.exchangingrate.di.component.DaggerActivityComponent
import com.Sam.exchangingrate.models.CurrencyRatesResponseModel
import com.Sam.exchangingrate.models.SingletonVariable
import com.Sam.exchangingrate.models.SingletonVariable.valueEntered
import com.Sam.exchangingrate.presenters.Presenter
import com.Sam.exchangingrate.presenters.PresenterImpl
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import javax.inject.Inject


class MainActivity : AppCompatActivity() , Presenter {

    @Inject lateinit var mainPresenterImpl: PresenterImpl
    lateinit var currencyRatesAdapter : DataRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerActivityComponent.builder().build().inject(this)

        recyclerInitation()
        mainPresenterImpl.mainPresenter = this
        mainPresenterImpl.fetchCurrencyRates(true)

        observeOnValueChange()
    }

    private fun observeOnValueChange() {
        SingletonVariable.valueEntered.observe(this, Observer { valueEntered ->
            if(valueEntered!=null){
                recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            currencyRatesAdapter.notifyDataSetChanged()
                        }
//                        else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                            Log.d("scroll", "dragging")
//                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
//                        Log.d("scroll", "scrolling")
                    }
                })
            }

        })

    }


    private fun recyclerInitation() {
        recycler_view.apply {
            setHasFixedSize(true)
            val viewManager = LinearLayoutManager(this@MainActivity)
            var arrayList = ArrayList<Pair<String, Double>>()
            arrayList.add(Pair("EUR",1.0))

            // use a linear layout manager
            layoutManager = viewManager as RecyclerView.LayoutManager?
            // specify an viewAdapter
            currencyRatesAdapter = DataRecyclerAdapter(arrayList)
            adapter = currencyRatesAdapter

        }
    }

    override fun dataFetchedSuccessfully(currencyRateResponseModel: CurrencyRatesResponseModel?) {
        if(currencyRateResponseModel!=null)
            currencyRatesAdapter.updateList(currencyRateResponseModel.rates!!.toList())
        else{
            currencyRatesAdapter.notifyDataSetChanged()
        }

    }

}
