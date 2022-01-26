package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val INITIAL_TIP_PERCENT = 16
class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount : EditText
    private lateinit var tvTipPercentLabel : TextView
    private lateinit var seekBarTip : SeekBar
    private lateinit var tvTipAmount : TextView
    private lateinit var tvTotalAmount : TextView
    private lateinit var tvTipDescription : TextView
    private lateinit var tvEmoji : TextView
    private lateinit var etPerson : EditText
    private lateinit var tvPerHeadAmount : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etPerson = findViewById(R.id.etPerson)
        tvPerHeadAmount = findViewById(R.id.tvPerHeadAmount)
        tvEmoji = findViewById(R.id.tvEmoji)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
             tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                computePerHead()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
               computeTipAndTotal()
                computePerHead()

            }

        } )

        etPerson.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                computePerHead()
            }
        })
    }

    private fun computePerHead() {
        if(etPerson.text.isEmpty()){
            tvPerHeadAmount.text = ""
            return
        }
        val total = tvTotalAmount.text.toString().toDouble()
        val numOfPerson = etPerson.text.toString().toDouble()
        val perHead = total / numOfPerson
        tvPerHeadAmount.text = "%.2f".format(perHead)
    }

    private fun updateTipDescription(tipPercent : Int) {
        val tipDescription: String
      when (tipPercent){
          in 0..9 -> tipDescription = "Poor"
          in 10..14 -> tipDescription = "Acceptable"
          in 15..19 -> tipDescription = "Good"
          in 20..24 -> tipDescription = "Great"
          else -> tipDescription = "Amazing"
      }

        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this,R.color.colorWorstTip),
            ContextCompat.getColor(this,R.color.colorBestTip)
        )as Int
        tvTipDescription.setTextColor(color)
        val emoji : String
        when(tipPercent){
            in 0..9 -> emoji = "\uD83D\uDE1E"
            in 10..14 -> emoji = "\uD83D\uDE42"
            in 15..19 -> emoji ="\uD83D\uDE0A"
            in 20..24 -> emoji = "\uD83D\uDE0D"
            else -> emoji = "\uD83E\uDD29"
        }
        tvEmoji.text= emoji
    }

    private fun updateEmoji(tipEmoji: String){

        }


            private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = "0"
            tvTotalAmount.text = "0"
            return
        }
     val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = tipPercent * baseAmount / 100
        val totalAmount = tipAmount + baseAmount
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}