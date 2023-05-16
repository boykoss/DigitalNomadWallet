package com.example.digitalnomadwallet.db.converters

import androidx.room.TypeConverter
import com.example.digitalnomadwallet.util.TransactionCategory

class Converters {

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String {
    return "${category.description}"
    }

    @TypeConverter
    fun toTransactionCategory(value: String): TransactionCategory {
        val list = value.split(",")
        val description = list[0].toInt()
        return TransactionCategory(description)
    }
}