package com.Sam.exchangingrate.models

import io.reactivex.subjects.PublishSubject

object ObservableObject{
    var valueEntered : Double? = 0.0
    var mObservable: PublishSubject<Double>? = PublishSubject.create<Double>()
    var isScrolling  :Boolean = false
}
