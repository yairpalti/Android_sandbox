
package com.yairpalti.calculator

import android.icu.text.NumberFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private var lastOperation = ""
    private var firstValue:Double = 0.0
    private var isNewOperation = true
    private var isDotExist = false

    fun numberButtonEvent(view:View) {
        var numberStr = tvShowValue.text.toString()
        // Clear current value if needed
        if (numberStr == "0" || isNewOperation) {
            numberStr = ""
            isDotExist = false
        }
        isNewOperation = false
        // Update the value according to current click
        val buttonPressed:Button = view as Button
        var numberButtonStr = buttonPressed.tag.toString()
        if (buttonPressed.id == buttonDot.id)  {
            if (isDotExist)
                return
            isDotExist = true
        }
        tvShowValue.setText(numberStr + numberButtonStr)
    }
    fun equalButtonEvent(@Suppress("UNUSED_PARAMETER") view:View) {
        var currNumberString = tvShowValue.text.toString()
        var finalNumber:Double = 0.0
        if (lastOperation == "")
            return
        when (lastOperation) {
            "+" -> {
                finalNumber = firstValue + currNumberString.toDouble()
            }
            "-" -> {
                finalNumber = firstValue - currNumberString.toDouble()
            }
            "*" -> {
                finalNumber = firstValue * currNumberString.toDouble()
            }
            "/" -> {
                finalNumber = firstValue / currNumberString.toDouble()
            }
        }
        // TODO - clean the .0 when not needed
        val finalNumberStr = java.text.NumberFormat.getInstance().format(finalNumber)
        tvShowValue.setText(finalNumberStr)
        isNewOperation = true
    }

    fun operationButtonEvent(view:View){
        val buttonPressed: Button = view as Button
        lastOperation = buttonPressed.tag.toString()
        firstValue = tvShowValue.text.toString().toDouble()
        isNewOperation = true
    }
    fun notNumberButtonEvent(view:View) {
        val buttonPressed:Button = view as Button
        var valueToShow = tvShowValue.text.toString()
        when (buttonPressed.id) {
            buttonAC.id -> {
                valueToShow = "0"
                lastOperation = ""
                firstValue = 0.0
                isNewOperation = true
                isDotExist = false
            }
            buttonPerc.id -> {
                val number:Double = valueToShow.toDouble()/100.0
                valueToShow = number.toString()
                isNewOperation = true
            }
            buttonPlusMinus.id -> {
                val number:Double = valueToShow.toDouble()
                if (number != 0.0)
                    valueToShow = (-number).toString()
            }
/*            buttonPlus.id -> {
                lastOperation = "+"
                firstValue = valueToShow.toDouble()
                isNewOperation = true
            }
            buttonDiv.id -> {
                lastOperation = "/"
                firstValue = valueToShow.toDouble()
                isNewOperation = true
            }
            buttonMinus.id -> {
                lastOperation = "-"
                firstValue = valueToShow.toDouble()
                isNewOperation = true
            }
            buttonMultiple.id -> {
                lastOperation = "*"
                firstValue = valueToShow.toDouble()
                isNewOperation = true
            }*/
        }
        tvShowValue.setText(valueToShow)
    }
}
