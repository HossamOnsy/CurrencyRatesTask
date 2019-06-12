package com.Sam.exchangingrate.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.models.CurrencyRatesRecyclerObject
import com.Sam.exchangingrate.utils.AppUtils.getFlagOfCurrencyHost
import com.Sam.exchangingrate.utils.AppUtils.getNameOfCurrency
import com.Sam.exchangingrate.utils.AppUtils.getValueAfterConversion
import com.Sam.exchangingrate.utils.currencyKey
import kotlinx.android.synthetic.main.currency_item.view.*
import java.text.DecimalFormat


class CurrencyConverterRecyclerAdapter(
    var currenciesList: ArrayList<CurrencyRatesRecyclerObject>,
    var currencyConverterAdapterListener: CurrenyConverterAdapterListener,
    var currentValue: Double
) :
    RecyclerView.Adapter<CurrencyConverterRecyclerAdapter.CurrencyItemViewHolder>() {

    var currencyRateMapping: Map<String, Double>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(
                com.Sam.exchangingrate.R.layout.currency_item,
                parent,
                false
            ) as View

        return CurrencyItemViewHolder(cardView)
    }


    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {

        checkEditTextEnablement(position, holder)
        reformatEditText(position, holder)
        bindValuesToView(position, holder)
        checkIfFirstResponder(position, holder)

        holder.itemView.setOnClickListener {
            itemClicked(position, holder)
        }

    }

    private fun bindValuesToView(
        position: Int,
        holder: CurrencyItemViewHolder
    ) {

        holder.title.text = currenciesList.get(position).name
        holder.flagIconIv.setImageResource(getFlagOfCurrencyHost(currenciesList.get(position).name))
        holder.currencyDescriptionTv.text = getNameOfCurrency(currenciesList.get(position).name)
    }


    private fun checkIfFirstResponder(position: Int, holder: CurrencyItemViewHolder) {

        if (position == 0)
            holder.et_value.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (holder.title.text.equals(currencyKey))
                        if (s.toString().length <= 0) {
                            currencyConverterAdapterListener.valueChanged(0.0)
                        }
                    currencyConverterAdapterListener.isUpdatingText(false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val tempValue = s.toString()
                    if (holder.title.text.equals(currencyKey) && tempValue.toDoubleOrNull() != null) {

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

    private fun checkEditTextEnablement(position: Int, holder: CurrencyItemViewHolder) {
        if (position != 0) {
            currenciesList.get(position).value =
                getValueAfterConversion(currentValue, currencyRateMapping, currenciesList.get(position).name).toString()
            holder.et_value.isEnabled = false
        } else {
            currenciesList.get(position).value = currentValue.toString()
            holder.et_value.isEnabled = true
            holder.et_value.requestFocus()
        }
    }

    private fun reformatEditText(position: Int, holder: CurrencyItemViewHolder) {
        if (currenciesList.get(position).value.equals("") || currenciesList.get(position).value.equals("0.0")) {
            holder.et_value.setText("")
            holder.et_value.hint = "0"
        } else {
            val amount = java.lang.Double.parseDouble(currenciesList.get(position).value)
            val formatter = DecimalFormat("###.00")
            val formatted = formatter.format(amount)

            if (position != 0)
                holder.et_value.setText(formatted)
            else
                holder.et_value.setText(currenciesList.get(position).value)
        }

    }


    private fun itemClicked(position: Int, holder: CurrencyItemViewHolder) {

        if (!holder.et_value.text.toString().trim().equals("")) {
            currencyConverterAdapterListener.valueChanged(holder.et_value.text.toString().toDouble())
        }
        currencyKey = currenciesList.get(position).name
        currenciesList.add(0, currenciesList.get(position))
        currenciesList.removeAt(position + 1)
        notifyItemMoved(position, 0)
        notifyItemChanged(0)
        notifyItemChanged(1)

        currencyConverterAdapterListener.listAltered()
    }

    fun updateList(pairList: ArrayList<CurrencyRatesRecyclerObject>) {
        this.currenciesList.addAll(pairList)
        notifyDataSetChanged()
    }

    class CurrencyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title_tv
        var currencyDescriptionTv = itemView.currency_desc_tv
        var et_value = itemView.et_value
        var flagIconIv = itemView.flag_icon
    }

    interface CurrenyConverterAdapterListener {
        fun isUpdatingText(textIsChanging: Boolean)
        fun valueChanged(valueEntered: Double)
        fun listAltered()
    }


    override fun getItemCount() = currenciesList.size

    fun valueAltered(
        value: Double?,
        arrayList: ArrayList<CurrencyRatesRecyclerObject>
    ) {
        if (value != null)
            currentValue = value
        else
            currentValue = 0.0

        notifyItemRangeChanged(1, arrayList.size)
    }


    fun currencyLatestDataConversion(it: Map<String, Double>) {
        currencyRateMapping = it
    }

}