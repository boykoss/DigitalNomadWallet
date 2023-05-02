package com.example.digitalnomadwallet.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions_table")
data class TransactionModel(

@PrimaryKey(autoGenerate = true)
var id: Int = 0,
var title: String,
var amount: Double,
var date: String,
var transactionType: String,
var category: TransactionCategory,
var note: String?,
) :java.io.Serializable
