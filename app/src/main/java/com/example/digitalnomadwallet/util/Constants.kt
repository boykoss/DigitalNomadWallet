package com.example.digitalnomadwallet.util

import android.content.Context

class Constants(context: Context) {

    val TRANSACTION_TYPE = listOf(
        "Income",
        "Expenses"
    )

    val TRANSACTION_CATEGORY = arrayListOf(
        context.getString(TransactionCategory.Food.description),
        context.getString(TransactionCategory.Health.description),
        context.getString(TransactionCategory.Rent.description),
        context.getString(TransactionCategory.Transportation.description),
        context.getString(TransactionCategory.Salary.description),
        context.getString(TransactionCategory.Other.description)
    )
}