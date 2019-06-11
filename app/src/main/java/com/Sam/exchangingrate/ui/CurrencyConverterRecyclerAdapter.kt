package com.Sam.exchangingrate.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.utils.AppUtils.getValueAfterConversion
import com.Sam.exchangingrate.utils.currencyKey
import kotlinx.android.synthetic.main.data_item.view.*
import timber.log.Timber
import java.text.DecimalFormat


class CurrencyConverterRecyclerAdapter(
    val activity: MainActivity,
    var valueList: ArrayList<CurrencyRatesRecyclerObject>,
    var currencyConverterAdapterListener: CurrenyConverterAdapterListener,
    var initalValueEntered: Double
) :
    RecyclerView.Adapter<CurrencyConverterRecyclerAdapter.DataViewHolder>() {

    var currencyRateMapping : Map<String, Double>? =null;

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(
                com.Sam.exchangingrate.R.layout.data_item,
                parent,
                false
            ) as View

        return DataViewHolder(cardView)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        checkEditTextEnablement(position, holder)
        reformatEditText(position, holder)
        holder.title.text = valueList.get(position).name
        checkIfFirstResponder(position, holder)

        holder.itemView.setOnClickListener {
            itemClicked(position, holder)
        }

    }

    private fun checkIfFirstResponder(position: Int, holder: DataViewHolder) {


            holder.et_value.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (position == 0 && holder.title.text.equals(currencyKey))
                        currencyConverterAdapterListener.isUpdatingText(false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val tempValue = s.toString()
                    if (position == 0 && holder.title.text.equals(currencyKey) && tempValue.toDoubleOrNull() != null) {

                        currencyConverterAdapterListener.isUpdatingText(true)
                        var temp = s.toString()
                        temp = temp.replace(".", "")
                        if (temp.length > 0) {
                            currencyConverterAdapterListener.valueChanged(s.toString().toDouble())
                        } else {
                            currencyConverterAdapterListener.valueChanged(0.0)
                        }
                    }
                }
            })
    }

    private fun checkEditTextEnablement(position: Int, holder: DataViewHolder) {
        if (position != 0) {
            valueList.get(position).value = getValueAfterConversion(initalValueEntered,currencyRateMapping,valueList.get(position).name).toString()
            holder.et_value.isEnabled = false
        } else {
            valueList.get(position).value = initalValueEntered.toString()
            holder.et_value.isEnabled = true
        }
    }

    private fun reformatEditText(position: Int, holder: DataViewHolder) {
        if (valueList.get(position).value.equals("") || valueList.get(position).value.equals("0.0")) {
            holder.et_value.setText("")
            holder.et_value.hint = "0"
        } else {
            val amount = java.lang.Double.parseDouble(valueList.get(position).value)
            val formatter = DecimalFormat("###.00")
            val formatted = formatter.format(amount)

            if (position != 0)
                holder.et_value.setText(formatted)
            else
                holder.et_value.setText(valueList.get(position).value)
        }

    }

    private fun itemClicked(position: Int, holder: DataViewHolder) {

        if (!holder.et_value.text.toString().trim().equals("")) {
            currencyConverterAdapterListener.valueChanged(holder.et_value.text.toString().toDouble())
        }

        currencyKey = valueList.get(position).name
        valueList.add(0, valueList.get(position))
        valueList.removeAt(position + 1)

        activity.receiveNewList(valueList)

    }

    fun updateList(pairList: ArrayList<CurrencyRatesRecyclerObject>) {
        this.valueList.addAll(pairList)
        notifyDataSetChanged()
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var et_value = itemView.et_value
    }

    interface CurrenyConverterAdapterListener {
        fun isUpdatingText(textIsChanging: Boolean)
        fun valueChanged(valueEntered: Double)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = valueList.size

    fun valueAltered(
        value: Double?,
        arrayList: ArrayList<CurrencyRatesRecyclerObject>
    ) {
        if(value!=null)
            initalValueEntered = value
        else
            initalValueEntered = 0.0


        notifyItemRangeChanged(1,arrayList.size)
    }


    fun currencyLatestDataConversion(it: Map<String, Double>) {
        currencyRateMapping = it
    }
}