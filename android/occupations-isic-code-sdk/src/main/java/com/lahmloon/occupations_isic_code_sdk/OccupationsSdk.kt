package com.lahmloon.occupations_isic_code_sdk

import android.content.Context
import androidx.room.Room
import com.lahmloon.occupations_isic_code_sdk.occupations.OccupationsDb
import com.lahmloon.occupations_isic_code_sdk.occupations.data.Occupations

/**
 * Performs occupation search
 */
class OccupationsSdk {
    constructor(context: Context) {
        init(context)
    }

    companion object {

        private lateinit var db: OccupationsDb
        private var instance: OccupationsSdk? = null

        @JvmStatic
        fun instance(context: Context): OccupationsSdk {
            if (instance == null) {
                instance = OccupationsSdk(context)
            }
            return instance!!
        }

        private fun init (context: Context) {
            // Database engine
            db = Room
                .databaseBuilder(context, OccupationsDb::class.java, "occupations.db")
                // Set asset-file to copy database from
                .createFromAsset("databases/occupations.db")
                // How the database gets copied over:
                // 1. Every time the import script is run - the database version increases in BuildConfig
                // 2. The local database (if already there) is verified to have the same version
                // 3. As we have a version greater the migration is performed
                // 4. We don't supply any migration (fallbackToDestructiveMigration)
                //    so the file gets copied over
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    fun searchOccupationsByIsicCode(postcode: String, maxCount: Int): List<Occupations> {
        return db.occupationsDao().searchOccupationsByIsicCode(postcode, maxCount)
    }
}