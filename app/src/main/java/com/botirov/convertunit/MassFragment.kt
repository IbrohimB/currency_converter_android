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

class MassFragment : Fragment() {

    private lateinit var selectUnit: TextView
    private lateinit var toUnitTextView: TextView
    private lateinit var inputValue: EditText
    private lateinit var resultText: TextView
    private lateinit var convertButton: Button
    private lateinit var conversionMessage: TextView

    private val massUnits = arrayOf(
        "milligram (mg)", "gram (g)", "kilogram (kg)", "tonne (t)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        selectUnit = view.findViewById(R.id.selectUnit2)
        toUnitTextView = view.findViewById(R.id.toUnitTextView2)
        inputValue = view.findViewById(R.id.inputValue2)
        resultText = view.findViewById(R.id.resultText2)
        convertButton = view.findViewById(R.id.convertButton2)
        conversionMessage = view.findViewById(R.id.conversionMessage2)

        // Set onClickListeners
        selectUnit.setOnClickListener {
            showMassUnitSelectionDialog(selectUnit.text.toString()) { selectedUnit ->
                selectUnit.text = selectedUnit
            }
        }

        toUnitTextView.setOnClickListener {
            showMassUnitSelectionDialog(toUnitTextView.text.toString()) { selectedUnit ->
                toUnitTextView.text = selectedUnit
            }
        }

        convertButton.setOnClickListener {
            val inputValueStr = inputValue.text.toString()
            if (inputValueStr.isNotEmpty()) {
                val inputDouble = inputValueStr.toDoubleOrNull() ?: 0.0
                val result = performMassConversion(selectUnit.text.toString(), toUnitTextView.text.toString(), inputDouble)
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

    private fun showMassUnitSelectionDialog(currentSelection: String, onUnitSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Mass Unit")
            .setSingleChoiceItems(massUnits, massUnits.indexOf(currentSelection)) { dialog, which ->
                val selectedUnit = massUnits[which]
                onUnitSelected(selectedUnit)
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun performMassConversion(fromUnit: String, toUnit: String, value: Double): Double {
        // Conversion rates based on 1 kilogram (kg)
        val conversionRates = mapOf(
            "milligram (mg)" to 1e6,
            "gram (g)" to 1e3,
            "kilogram (kg)" to 1.0,
            "tonne (t)" to 1e-3
        )

        val fromRate = conversionRates[fromUnit] ?: return 0.0
        val toRate = conversionRates[toUnit] ?: return 0.0

        val valueInKilograms = value / fromRate
        return valueInKilograms * toRate
    }
}
