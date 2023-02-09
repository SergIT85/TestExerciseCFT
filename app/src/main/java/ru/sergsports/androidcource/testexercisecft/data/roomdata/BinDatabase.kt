package ru.sergsports.androidcource.testexercisecft.data.roomdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BinEntity::class], version = 1)
abstract class BinDatabase: RoomDatabase() {
    abstract fun BinDao(): BinDao

    companion object {
        private var instance: BinDatabase? = null

        @Synchronized
        fun get(context: Context): BinDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    BinDatabase::class.java, "BinDatabase"
                ).build()
            }
            return instance!!
        }
    }
}