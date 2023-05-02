package com.example.digitalnomadwallet.db

import android.content.Context
import androidx.room.*
import com.example.digitalnomadwallet.db.converters.Converters

@Database(entities = [TransactionModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class TransactionsDatabase : RoomDatabase() {

    abstract fun transactionsDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionsDatabase? = null

        fun getInstance(context: Context): TransactionsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionsDatabase::class.java,
                        "transactions_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}