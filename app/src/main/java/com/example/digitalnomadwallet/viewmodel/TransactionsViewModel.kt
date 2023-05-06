package com.example.digitalnomadwallet.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.digitalnomadwallet.db.TransactionModel
import com.example.digitalnomadwallet.repository.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class TransactionsViewModel(
    private val repository: TransactionsRepository,
    app: Application
) : AndroidViewModel(app){

    private val _period = MutableLiveData(Period.ofYears(5))
    var period = _period

    var transaction: LiveData<List<TransactionModel>> =
        getAllTransactions().asLiveData()

    private val _selectedCurrency = MutableStateFlow("USD")
    val selectedCurrency = _selectedCurrency

    init {
        transaction
    }

    fun filterAllTransactions(filterPeriod: Period? = period.value): LiveData<List<TransactionModel>> =
        if (filterPeriod != null) {
            repository.getAllTransactions().map { list ->
                list.filter { transaction ->
                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy")
                    val formattedDate = LocalDate.parse(transaction.date, dateFormatter)
                    val currentDate = LocalDate.now()

                    formattedDate in currentDate.minus(filterPeriod)..currentDate
                }
            }.asLiveData()
        } else repository.getAllTransactions().asLiveData()

    fun getAllTransactions() = repository.getAllTransactions()

    fun getTransactionById(id: Int) = repository.getTransactionById(id)

    fun insertTransaction(transaction: TransactionModel) =
        viewModelScope.launch { repository.insert(transaction) }

    fun deleteTransaction(transaction: TransactionModel) =
        viewModelScope.launch { repository.delete(transaction) }

    fun updateTransaction(transaction: TransactionModel) =
        viewModelScope.launch { repository.update(transaction) }



}