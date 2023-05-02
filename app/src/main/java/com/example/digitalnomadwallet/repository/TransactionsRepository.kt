package com.example.digitalnomadwallet.repository

import com.example.digitalnomadwallet.db.TransactionModel
import com.example.digitalnomadwallet.db.TransactionsDatabase
import kotlinx.coroutines.flow.Flow

class TransactionsRepository(private val db: TransactionsDatabase) {

    suspend fun insert(transaction: TransactionModel) = db.transactionsDao().insert(transaction)

    suspend fun update(transaction: TransactionModel) = db.transactionsDao().update(transaction)

    suspend fun  delete(transaction: TransactionModel) = db.transactionsDao().delete(transaction)

    fun getAllTransactions(): Flow<List<TransactionModel>> = db.transactionsDao().getAllTransactions()

    fun getTransactionById(id: Int): Flow<List<TransactionModel>> = db.transactionsDao().getTransactionById(id)
}