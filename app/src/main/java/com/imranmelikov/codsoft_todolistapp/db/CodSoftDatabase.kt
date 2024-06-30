package com.imranmelikov.codsoft_todolistapp.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database([Task::class], version = 1)
abstract class CodSoftDatabase:RoomDatabase() {
    abstract fun dao():CodSoftDao
}