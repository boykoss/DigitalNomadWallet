package com.example.digitalnomadwallet.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalnomadwallet.MainActivity

import com.example.digitalnomadwallet.R
import com.example.digitalnomadwallet.adapters.TransactionItemAdapter
import com.example.digitalnomadwallet.databinding.FragmentDashboardBinding
import com.example.digitalnomadwallet.model.TransactionModel
import com.example.digitalnomadwallet.util.currencyFormat
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.Period

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: TransactionItemAdapter
    private lateinit var viewModel: TransactionsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        setUpViews()
        swipeToDelete()
        binding.fabAddTransaction.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_addTransactionFragment)
        }
    }

    private fun setUpRecyclerView() = lifecycleScope.launch {
        viewModel.selectedCurrency.collect {
            adapter = TransactionItemAdapter()
            adapter.currency = it
            binding.rvTransactions.adapter = adapter
            binding.rvTransactions.layoutManager = LinearLayoutManager(activity)
            adapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("transaction", it)
                }
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_transactionDetailFragment,
                    bundle
                )
            }
        }
    }

    private fun setPeriodFilter() = with(binding) {
        chipsTransactionsFilter.filterChipGroup.setOnCheckedChangeListener() { _, checkedId ->
            when(checkedId) {
                chipsTransactionsFilter.chipAllTransactions.id -> viewModel.period.postValue(
                    Period.ofYears(
                        10
                    )
                )
                chipsTransactionsFilter.chipTenDays.id -> viewModel.period.postValue(
                    Period.ofDays(
                        10
                    )
                )
                chipsTransactionsFilter.chipThirtyDays.id -> viewModel.period.postValue(
                    Period.ofDays(
                        30
                    )
                )
                chipsTransactionsFilter.chipSixtyDays.id -> viewModel.period.postValue(
                    Period.ofDays(
                        60
                    )
                )
                else -> viewModel.period.postValue(Period.ofYears(10))

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews() = with(binding) {
        chipsTransactionsFilter.chipAllTransactions.isChecked = true
        lifecycleScope.launch {
            viewModel.selectedCurrency.collect { selectedCurrency ->
                viewModel.period.observe(viewLifecycleOwner) { period ->
                    viewModel.transaction.observe(viewLifecycleOwner) { transaction ->

                        setPeriodFilter()

                        val (totalIncome, totalExpense) =
                            transaction.partition { it.transactionType == "Income" }
                        val income: Double = totalIncome.sumOf { it.amount }
                        val expense: Double = totalExpense.sumOf { it.amount }
                        val balance = income - expense

                        itemIncomeCardView.tvTotalIncome.text =
                            currencyFormat(income, selectedCurrency)
                        itemExpenseCardView.tvTotalExpense.text =
                            currencyFormat(expense, selectedCurrency)
                        totalBalanceView.tvTotalBalance.text =
                            currencyFormat(balance, selectedCurrency)
                    }
                    viewModel.filterAllTransactions(period)
                        .observe(viewLifecycleOwner) { transaction ->
                            adapter.differ.submitList(transaction)
                        }
                }
            }
        }
    }

    private fun swipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = adapter.differ.currentList[position]
                val transactionItem = TransactionModel(
                    id = transaction.id,
                    title = transaction.title,
                    transactionType = transaction.transactionType,
                    category = transaction.category,
                    amount = transaction.amount,
                    note = transaction.note,
                    date = transaction.date
                )
                viewModel.deleteTransaction(transactionItem)

                Snackbar.make(
                    binding.root,
                    getString(R.string.transaction_deleted),
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction(getString(R.string.undo)) {
                        viewModel.insertTransaction(transactionItem)
                    }
                }.show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvTransactions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_top_menu, menu)
        lifecycleScope.launch {
            viewModel.selectedCurrency.collect {
                menu.getItem(0).title = it
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToBottomSheetFragment()
        return when (item.itemId) {
            R.id.action_currency -> {
                requireView().findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }






}