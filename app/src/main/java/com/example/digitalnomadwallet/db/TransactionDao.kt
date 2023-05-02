package com.example.digitalnomadwallet.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionModel: TransactionModel)

    @Update
    suspend fun update(transactionModel: TransactionModel)

    @Delete
    suspend fun delete(transactionModel: TransactionModel)

    @Query("SELECT * FROM transactions_table")
    fun getAllTransactions(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM transactions_table WHERE id = :id")
    fun getTransactionById(id: Int): Flow<List<TransactionModel>>

    @Query("SELECT * FROM transactions_table WHERE category = :category ORDER BY date ASC")
    fun getAllTransactionsByCategory(category: String): LiveData<List<TransactionModel>>
}