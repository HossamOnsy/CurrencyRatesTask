package com.Sam.exchangingrate.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.models.ObservableObject
import com.Sam.exchangingrate.models.ObservableObject.valueEntered
import com.Sam.exchangingrate.models.textWatching
import com.Sam.exchangingrate.utils.AppUtils.getValueAfterConversion
import com.Sam.exchangingrate.utils.currencyKey
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.data_item.view.*
import timber.log.Timber
import java.text.DecimalFormat


class DataRecyclerAdapter(
    val activity: MainActivity,
    var valueList: ArrayList<CurrencyRatesRecyclerObject>
) :
    RecyclerView.Adapter<DataRecyclerAdapter.DataViewHolder>() {
    companion object {
        var firstTimeListener = true
        var textChanged = false
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(com.Sam.exchangingrate.R.layout.data_item, parent, false) as View


        return DataViewHolder(cardView)
    }

    fun updateList(pairList: ArrayList<CurrencyRatesRecyclerObject>) {
        this.valueList.addAll(pairList)
        notifyDataSetChanged()
    }

    var t: PublishSubject<Double>? = null
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {


        if (position != 0) {
            valueList.get(position).value = getValueAfterConversion(valueList.get(position).name).toString()
            holder.et_value.isEnabled = false
        } else {
            valueList.get(position).value = valueEntered.toString()
            holder.et_value.isEnabled = true

        }


        if (valueList.get(position).value.equals("") || valueList.get(position).value.equals("0.0")) {
            holder.et_value.setText("")
            holder.et_value.hint = "0"
        } else {
            val amount = java.lang.Double.parseDouble(valueList.get(position).value)
            val formatter = DecimalFormat("###.00")
            val formatted = formatter.format(amount)

            if(position!=0)
            holder.et_value.setText(formatted)
            else
                holder.et_value.setText(valueList.get(position).value)
        }

        holder.title.text = valueList.get(position).name

        Timber.d("checkPosition -> ${position}   -> getText -> ${valueList.get(position).value}")

        if (holder.title.text.equals(currencyKey) && position == 0 ) {
            Timber.d("checkPosition -> ${position}   -> getText -> ${holder.title.text}")
//            if (valueEntered != null)
//                holder.et_value.setText(valueEntered!!.toString())
//            else {
//                holder.et_value.setText("")
//                holder.et_value.hint = "0"
//            }

            addTextChangedListener(holder.et_value, position, valueList.get(position).name)

        } else
//            if (!holder.title.text.equals(currencyKey) && position > 0)
        {

            holder.et_value.removeTextChangedListener(textWatching)
            Timber.d("checkPosition else -> ${position}   -> getText -> ${holder.title.text}")


//            if (t == null) {
//                t = ObservableObject.mObservable!!
//            }
//            t!!.distinct().debounce (300, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe{
//                        value ->
//
//                    holder.et_value.setText(getResultAfterConversion(valueList.get(position).name, value).toString())
//                }

//            }

        }

        holder.itemView.setOnClickListener {
            itemClicked(position, holder)
        }

    }

    private fun itemClicked(position: Int, holder: DataViewHolder) {

        if (!holder.et_value.text.toString().trim().equals(""))
            valueEntered = holder.et_value.text.toString().toDouble()

        currencyKey = valueList.get(position).name
        valueList.add(0, valueList.get(position))
        valueList.removeAt(position + 1)
        firstTimeListener = true

        sendNewList(valueList)

        Timber.d("Testing ${valueList.get(0).name}")
    }

    fun sendNewList(valueList: ArrayList<CurrencyRatesRecyclerObject>) {

        activity.receiveNewList(valueList)
    }


    private fun addTextChangedListener(et_value: EditText?, position: Int, first: String) {

        Timber.d("Testing ${valueList.get(position).name}")

        if (firstTimeListener) {
//            firstTimeListener = false
            if (et_value != null)
                et_value.addTextChangedListener(textWatching)
        }
//
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var et_value = itemView.et_value
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = valueList.size
}