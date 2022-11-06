package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBillAMount: EditText
    private lateinit var tipSeekBar: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalBill: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitialize()
        seekBarListener()
        etBillListener()

    }

    private fun viewInitialize() {
        etBillAMount = findViewById(R.id.ed_bill_amount)
        tipSeekBar = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.txt_tip_percent)
        tvTipAmount = findViewById(R.id.tip_text)
        tvTotalBill = findViewById(R.id.total_text)
        tvTipDescription = findViewById(R.id.txt_tip_descr)
    }

    private fun seekBarListener() {
        tipSeekBar.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onprogress changed $p1")
                tvTipPercent.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescr = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
       tvTipDescription.text = tipDescr

        //update the color based on tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/tipSeekBar.max,
            ContextCompat.getColor(this,R.color.color_worst),
            ContextCompat.getColor(this,R.color.color_best)
        ) as Int
        tvTipDescription.setTextColor(color)
    }



    private fun etBillListener() {
        etBillAMount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "on edit text changed $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun computeTipAndTotal() {
        if (etBillAMount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalBill.text = ""
            return
        }
        //Get the value of the base and tip percent
        val billAmount = etBillAMount.text.toString().toDouble()
        val tipPercent = tipSeekBar.progress
        //Compute the tip and total
        val tipAmount = billAmount * tipPercent / 100
        val totalAmount = billAmount + tipAmount
        //Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalBill.text = "%.2f".format(totalAmount)


    }
}