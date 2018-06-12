
package com.yairpalti.calculator

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
    private var firstOperand:Double = 0.0
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
        var numberStr = tvShowValue.text.toString()
        var finalNumber:Double = 0.0
        if (lastOperation == "")
            return
        when (lastOperation) {
            "+" -> {
                finalNumber = firstOperand + numberStr.toDouble()
            }
            "-" -> {
                finalNumber = firstOperand - numberStr.toDouble()
            }
            "*" -> {
                finalNumber = firstOperand * numberStr.toDouble()
            }
            "/" -> {
                finalNumber = firstOperand / numberStr.toDouble()
            }
        }
        // Format the number to not have .0
        val finalNumberStr = java.text.NumberFormat.getInstance().format(finalNumber)
        tvShowValue.setText(finalNumberStr)
        isNewOperation = true
    }

    fun operationButtonEvent(view:View){
        val buttonPressed: Button = view as Button
        lastOperation = buttonPressed.tag.toString()
        firstOperand = tvShowValue.text.toString().toDouble()
        isNewOperation = true
    }
    fun otherButtonEvent(view:View) {
        val buttonPressed:Button = view as Button
        var numberStr = tvShowValue.text.toString()
        when (buttonPressed.id) {
            buttonAC.id -> {
                numberStr = reset()
            }
            buttonPerc.id -> {
                val number:Double = numberStr.toDouble()/100.0
                numberStr = number.toString()
                isNewOperation = true
            }
            buttonPlusMinus.id -> {
                val number:Double = numberStr.toDouble()
                if (number != 0.0)
                    numberStr = (-number).toString()
            }
        }
        tvShowValue.setText(numberStr)
    }

    private fun reset(): String {
        val numberStr = "0"
        lastOperation = ""
        firstOperand = 0.0
        isNewOperation = true
        isDotExist = false
        return numberStr
    }
}
