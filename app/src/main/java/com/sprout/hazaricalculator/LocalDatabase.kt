package com.sprout.hazaricalculator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Score::class], version = 2)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun ScoreDao(): ScoreDao

    companion object{
        private var instance : LocalDatabase? = null

        fun getInstance(ctx: Context): LocalDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(ctx.applicationContext, LocalDatabase::class.java, "local_database")
                    .fallbackToDestructiveMigration()
                           .build()
            }
            return instance!!
        }

    }
}