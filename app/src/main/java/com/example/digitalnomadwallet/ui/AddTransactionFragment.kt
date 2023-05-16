package com.example.digitalnomadwallet.ui

import android.app.DatePickerDialog
import android.os.Bundle
import java.lang.Double.parseDouble
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController

import com.example.digitalnomadwallet.MainActivity
import com.example.digitalnomadwallet.R
import com.example.digitalnomadwallet.databinding.FragmentAddTransactionBinding
import com.example.digitalnomadwallet.util.TransactionCategory
import com.example.digitalnomadwallet.model.TransactionModel
import com.example.digitalnomadwallet.util.Constants
import com.example.digitalnomadwallet.util.getCurrencySymbol
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*


class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var viewModel: TransactionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddTransactionBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setUpView()
    }

    private fun setUpView() {
        with(binding) {
            inputFields.etTransactionCategory.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    Constants(requireContext()).TRANSACTION_CATEGORY
                )
            )
            inputFields.etTransactionType.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    Constants(requireContext()).TRANSACTION_TYPE
                )
            )
            inputFields.etTransactionDate.apply {
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = false
                val myCalendar = Calendar.getInstance()
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val datePickerOnDateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        myCalendar.set(Calendar.YEAR, year)
                        myCalendar.set(Calendar.MONTH, monthOfYear)
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        setText(sdf.format(myCalendar.time))
                    }
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    datePickerOnDateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.datePicker.maxDate = Date().time
                setOnClickListener {
                    datePickerDialog.show()
                }
            }
            lifecycleScope.launch {
                viewModel.selectedCurrency.collect {
                    inputFields.tilTransactionAmount.prefixText = getCurrencySymbol(it)
                }
            }

            btnSaveTransaction.setOnClickListener {
                validateFields()
                insertTransaction()
                if (!validateFields()) {
                    view?.findNavController()?.navigateUp()
                }
            }
            }
        }

    private fun getNoteFromData(): TransactionModel = binding.inputFields.let {
        val title = it.etTransactionTitle.text.toString()
        val amount = parseDouble(it.etTransactionAmount.text.toString())
        val date = it.etTransactionDate.text.toString()
        val category = when (it.etTransactionCategory.text.toString()) {
            getString(R.string.food) -> TransactionCategory.Food
            getString(R.string.rent) -> TransactionCategory.Rent
            getString(R.string.health) -> TransactionCategory.Health
            getString(R.string.other) -> TransactionCategory.Other
            getString(R.string.salary) -> TransactionCategory.Salary
            getString(R.string.transportation) -> TransactionCategory.Transportation
            else -> TransactionCategory.Other
    }
        val type = when (it.etTransactionType.text.toString()) {
            getString(R.string.income) -> "Income"
            getString(R.string.expenses) -> "Expense"
            else -> "other"
        }
        val note = it.etTransactionNotes.text.toString()

        return TransactionModel(
            title = title,
            transactionType = type,
            amount = amount,
            category = category,
            date = date,
            note = note
        )
    }

    private fun validateFields(): Boolean {
        when {
            binding.inputFields.etTransactionAmount.text.isNullOrEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    R.string.enter_amount,
                    Toast.LENGTH_SHORT
                ).show()
                binding.inputFields.etTransactionAmount.error = getString(R.string.required_field)
            }
            binding.inputFields.etTransactionTitle.text.isNullOrEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.missing_field),
                    Toast.LENGTH_SHORT
                ).show()
                binding.inputFields.etTransactionTitle.error = getString(R.string.required_field)
            }
            binding.inputFields.etTransactionCategory.text.isNullOrEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.missing_category),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            binding.inputFields.etTransactionType.text.isNullOrEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.missing_transaction_type),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.inputFields.etTransactionDate.text.isNullOrEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_date),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        return with(binding.inputFields) {
            etTransactionAmount.text.isNullOrEmpty() or
                    etTransactionCategory.text.isNullOrEmpty() or
                    etTransactionDate.text.isNullOrEmpty() or
                    etTransactionTitle.text.isNullOrEmpty() or
                    etTransactionType.text.isNullOrEmpty()
        }
    }

    private fun insertTransaction() {
        if (
            with(binding.inputFields) {
                etTransactionAmount.text!!.isNotEmpty()
                        etTransactionCategory.text!!.isNotEmpty()
                        etTransactionDate.text!!.isNotEmpty()
                        etTransactionTitle.text!!.isNotEmpty()
                        etTransactionType.text!!.isNotEmpty()
            }
        ) {
            viewModel.insertTransaction(getNoteFromData())
            Snackbar.make(
                requireView(),
                getString(R.string.transaction_saved),
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

}