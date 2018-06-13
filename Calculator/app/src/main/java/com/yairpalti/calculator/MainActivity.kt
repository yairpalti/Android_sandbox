
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
    private var isNewOperation = false
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
    private fun updateOperationAndOperandText() {
        if (lastOperation == "")
            tvOperation.setText("")
        else
            tvOperation.setText(firstOperand.format() + lastOperation)
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
        showNumber(finalNumber)
        // clear the operation (not to allow twice '=')
        lastOperation = ""
        updateOperationAndOperandText()
        isNewOperation = true
    }
    // Extension function to Double
    private fun Double.format():String = java.text.NumberFormat.getInstance().format(this)
    private fun showNumber(number:Double) {
        // Format the number to not have .0
        tvShowValue.setText(number.format())
    }
    fun operationButtonEvent(view:View) {
        val buttonPressed: Button = view as Button
        lastOperation = buttonPressed.tag.toString()
        firstOperand = tvShowValue.text.toString().toDouble()
        isNewOperation = true
        updateOperationAndOperandText()
    }
    fun otherButtonEvent(view:View) {
        val buttonPressed:Button = view as Button
        var number:Double = tvShowValue.text.toString().toDouble()
        when (buttonPressed.id) {
            buttonAC.id -> {
                number = reset().toDouble()
            }
            buttonPerc.id -> {
                number /= 100.0
                isNewOperation = true
            }
            buttonPlusMinus.id -> {
                if (number != 0.0)
                    number = -number
            }
        }
        showNumber(number)
    }

    private fun reset(): String {
        val numberStr = "0"
        lastOperation = ""
        firstOperand = 0.0
        isNewOperation = false
        isDotExist = false
        tvOperation.setText("")
        return numberStr
    }
}
