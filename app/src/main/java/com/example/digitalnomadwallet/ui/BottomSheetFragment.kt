package com.example.digitalnomadwallet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.digitalnomadwallet.MainActivity
import com.example.digitalnomadwallet.R
import com.example.digitalnomadwallet.databinding.FragmentBottomSheetBinding
import com.example.digitalnomadwallet.databinding.ListItemTransactionBinding
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var viewModel: TransactionsViewModel
    lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setUpView()
    }

    private fun setUpView() = with(binding) {
        lifecycleScope.launch{



            btnUSD.setOnClickListener {
                viewModel.selectedCurrency.value = "USD"
                dismiss()
            }

            btnRUB.setOnClickListener {
                viewModel.selectedCurrency.value = "RUB"
                dismiss()
            }

            btnTHB.setOnClickListener {
                viewModel.selectedCurrency.value = "THB"
                dismiss()
            }

            btnMYR.setOnClickListener {
                viewModel.selectedCurrency.value = "MYR"
                dismiss()
            }


        }

    }


    }