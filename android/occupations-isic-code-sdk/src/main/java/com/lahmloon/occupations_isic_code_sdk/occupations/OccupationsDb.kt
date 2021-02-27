package com.lahmloon.occupations_isic_code_sdk.occupations

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lahmloon.occupations_isic_code_sdk.occupations.dao.OccupationsDao
import com.lahmloon.occupations_isic_code_sdk.BuildConfig
import com.lahmloon.occupations_isic_code_sdk.occupations.data.Occupations

/**
 * Occupations database definition
 * Take a look at how the version is set - thus we have a new version every time we run import
 */
@Database(entities = [Occupations::class], version = BuildConfig.OCCUPATIONS_DB_VERSION)
abstract class OccupationsDb: RoomDatabase() {
    /**
     * Occupations DAO
     */
    abstract fun occupationsDao(): OccupationsDao
}