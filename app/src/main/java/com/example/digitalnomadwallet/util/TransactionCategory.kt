package com.example.digitalnomadwallet.util

import androidx.annotation.StringRes
import com.example.digitalnomadwallet.R

data class TransactionCategory(
    @StringRes
    val description: Int
){

    companion object {
        val Food = TransactionCategory(
        description = R.string.food
        )

        val Rent = TransactionCategory(
            description = R.string.rent
        )

        val Health = TransactionCategory(
            description = R.string.health
        )

        val Transportation = TransactionCategory(
            description = R.string.transportation
        )

        val Other = TransactionCategory(
            description = R.string.other
        )

        val Salary = TransactionCategory(
            description = R.string.salary
        )

        val TRANSACTION_CATEGORIES = arrayListOf(
            Food,
            Transportation,
            Salary,
            Other
        )

    }

}