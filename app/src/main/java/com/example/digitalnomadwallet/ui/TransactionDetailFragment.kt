package com.example.digitalnomadwallet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.digitalnomadwallet.MainActivity
import com.example.digitalnomadwallet.R
import com.example.digitalnomadwallet.databinding.FragmentTransactionDetailsBinding
import com.example.digitalnomadwallet.util.currencyFormat
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModel
import kotlinx.coroutines.launch


class TransactionDetailFragment : Fragment(R.layout.fragment_transaction_details) {

    private lateinit var binding: FragmentTransactionDetailsBinding
    private val args: TransactionDetailFragmentArgs by navArgs()
    private lateinit var viewModel: TransactionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionDetailsBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        observeData()
    }

    private fun observeData() = with(binding) {
        val transactionItem = viewModel.getTransactionById(args.transaction.id)
        lifecycleScope.launch {
            transactionItem.collect { transaction ->
                tvDetailsTitle.text = transaction.title
                tvDetailsCategory.text = getString(transaction.category.description)
                tvDetailsNotes.text = transaction.note
                tvDetailsType.text = transaction.transactionType
                tvDetailsDate.text = transaction.date
                tvDetailsAmount.text = currencyFormat(transaction.amount,viewModel.selectedCurrency.value)

                val action =
                TransactionDetailFragmentDirections.actionTransactionDetailFragmentToEditTransactionFragment(
                    transaction
                )
                fabEditTransaction.setOnClickListener { findNavController().navigate(action) }
            }
        }
    }
}





