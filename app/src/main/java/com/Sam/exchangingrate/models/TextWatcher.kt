package com.Sam.exchangingrate.models

import android.text.Editable
import android.text.TextWatcher
import com.Sam.exchangingrate.ui.DataRecyclerAdapter
import timber.log.Timber

object textWatching : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        DataRecyclerAdapter.textChanged = true
        if (!ObservableObject.isScrolling) {
            var temp = s.toString()
//            DataRecyclerAdapter.textChanged = false
            Timber.d("onTextChanged -> ${temp} ")
            temp = temp.replace(".", "")
            if (temp.length > 0) {
                ObservableObject.valueEntered = s.toString().toDouble()
                ObservableObject.mObservable?.onNext(ObservableObject.valueEntered!!)
            } else {
                ObservableObject.valueEntered = 0.0
                ObservableObject.mObservable?.onNext(ObservableObject.valueEntered!!)
            }
        }


    }

}