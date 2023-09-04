package com.botirov.convertunit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LengthFragment : Fragment() {

    private lateinit var selectUnit: TextView
    private lateinit var toUnitTextView: TextView
    private lateinit var inputValue: EditText
    private lateinit var resultText: TextView
    private lateinit var convertButton: Button
    private lateinit var conversionMessage: TextView

    private val lengthUnits = arrayOf(
        "millimeter (mm)", "centimeter (cm)", "decimeter (dm)",
        "meter (m)", "hectometer (hm)", "kilometer (km)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_length, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        selectUnit = view.findViewById(R.id.selectUnit)
        toUnitTextView = view.findViewById(R.id.toUnitTextView)
        inputValue = view.findViewById(R.id.inputValue)
        resultText = view.findViewById(R.id.resultText)
        convertButton = view.findViewById(R.id.convertButton)
        conversionMessage = view.findViewById(R.id.conversionMessage)

        // Set onClickListeners
        selectUnit.setOnClickListener {
            showLengthUnitSelectionDialog(selectUnit.text.toString()) { selectedUnit ->
                selectUnit.text = selectedUnit
            }
        }

        toUnitTextView.setOnClickListener {
            showLengthUnitSelectionDialog(toUnitTextView.text.toString()) { selectedUnit ->
                toUnitTextView.text = selectedUnit
            }
        }

        convertButton.setOnClickListener {
            val inputValueStr = inputValue.text.toString()
            if (inputValueStr.isNotEmpty()) {
                val inputDouble = inputValueStr.toDoubleOrNull() ?: 0.0
                val result = performLengthConversion(selectUnit.text.toString(), toUnitTextView.text.toString(), inputDouble)
                val formattedResult = if (result % 1 == 0.0) String.format("%.0f", result) else String.format("%.2f", result)

                resultText.text = formattedResult

                // Set the conversion message
                conversionMessage.text = "$inputValueStr ${extractUnitAbbreviation(selectUnit.text.toString())} is equal to ${formattedResult} ${extractUnitAbbreviation(toUnitTextView.text.toString())}"
                conversionMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun extractUnitAbbreviation(fullUnit: String): String {
        return fullUnit.split(" ")[1].removeSurrounding("(", ")")
    }

    private fun showLengthUnitSelectionDialog(currentSelection: String, onUnitSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Length Unit")
            .setSingleChoiceItems(lengthUnits, lengthUnits.indexOf(currentSelection)) { dialog, which ->
                val selectedUnit = lengthUnits[which]
                onUnitSelected(selectedUnit)
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun performLengthConversion(fromUnit: String, toUnit: String, value: Double): Double {
        // Conversion rates based on 1 meter (m)
        val conversionRates = mapOf(
            "millimeter (mm)" to 1e3,
            "centimeter (cm)" to 1e2,
            "decimeter (dm)" to 1e1,
            "meter (m)" to 1.0,
            "hectometer (hm)" to 1e-2,
            "kilometer (km)" to 1e-3
        )

        val fromRate = conversionRates[fromUnit] ?: return 0.0
        val toRate = conversionRates[toUnit] ?: return 0.0

        val valueInMeters = value / fromRate
        return valueInMeters * toRate
    }

    // Helper function to extract only the unit name for the conversion message
    private fun extractUnitName(fullUnit: String): String {
        return fullUnit.split(" ")[0]
    }
}
