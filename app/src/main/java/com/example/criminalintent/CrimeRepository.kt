package com.example.criminalintent

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.criminalintent.database.CrimeDatabase
import com.example.criminalintent.database.migration_1_2
import com.example.criminalintent.database.migration_2_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context, private val coroutineScope: CoroutineScope = GlobalScope) {

    private val database: CrimeDatabase = Room.databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME)
        .addMigrations(migration_1_2, migration_2_3)
        .build()

    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    fun getCrime(id: UUID): Flow<Crime> = database.crimeDao().getCrime(id)
    fun updateCrimes(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().updateCrimes(crime)
        }
    }
    fun addCrime(crime: Crime){
        coroutineScope.launch {
            database.crimeDao().addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
