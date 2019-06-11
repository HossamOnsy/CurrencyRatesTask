package com.Sam.exchangingrate.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {


    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()


    fun onRetrieveDataError(error: Throwable?) {

        loadingVisibility.value = View.GONE
        if (error != null && error.message != null)
            errorMessage.value = error.message
        else
            errorMessage.value = "Error Occured"
    }


    fun onRetrieveDataFinish() {

        loadingVisibility.value = View.GONE
    }

    fun onRetrieveDataStart() {
        errorMessage.value = null
        loadingVisibility.value = View.VISIBLE
    }


}