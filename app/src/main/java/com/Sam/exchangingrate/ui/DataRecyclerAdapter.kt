package com.Sam.exchangingrate.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.Sam.exchangingrate.R
import com.Sam.exchangingrate.models.SingletonVariable.valueEntered
import com.Sam.exchangingrate.utils.AppUtils.getValueAfterConversion
import com.Sam.exchangingrate.utils.currencyKey
import kotlinx.android.synthetic.main.data_item.view.*
import timber.log.Timber
import kotlin.math.roundToInt


class DataRecyclerAdapter(
    private var valueList: ArrayList<Pair<String, Double>>
) :
    RecyclerView.Adapter<DataRecyclerAdapter.DataViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataRecyclerAdapter.DataViewHolder {
        var cardView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false) as View
        return DataViewHolder(cardView)
    }


    fun updateList(pairList: List<Pair<String, Double>>) {
        this.valueList.addAll(pairList)
        notifyDataSetChanged()
    }


    fun clearList() {
        this.valueList.clear()
        notifyDataSetChanged()
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        if (position == 0) {
            addTextChangedListener(holder.et_value,position,valueList[position].first)

        } else {
            Timber.d("checkPosition -> ${position}")
            if(!holder.title.text.equals(currencyKey))
                    holder.et_value.setText(getValueAfterConversion(valueList[position].first).toString())
        }
        holder.title.setText(valueList[position].first)

        holder.itemView.setOnClickListener {
            itemClicked(position)
        }

    }

    private fun itemClicked(position: Int) {

        var testing = valueEntered
        currencyKey = valueList.get(position).first
        valueList.add(0, valueList.get(position))
        valueList.removeAt(position+1)
        notifyItemRemoved(position)
        notifyItemInserted(0)


        Timber.d("Testing ${valueList.get(position).first}")
        Timber.d("Testing ${position}")
    }

    private fun addTextChangedListener(et_value: EditText?, position: Int, first: String) {
        et_value!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var temp = s.toString()

                if(temp.length>0)
                    valueEntered.value = temp.toDouble().toInt()
                else
                    valueEntered.value=0
                Timber.d("Position -> ${position}    first --->   ${first}")

            }

        })

    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var et_value = itemView.et_value

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = valueList.size
}