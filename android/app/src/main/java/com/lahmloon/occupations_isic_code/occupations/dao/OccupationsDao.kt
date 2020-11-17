package com.lahmloon.occupations_isic_code.occupations.dao

import androidx.room.Dao
import androidx.room.Query
import com.lahmloon.occupations_isic_code.occupations.data.Occupations

/**
 * Occupations DAO
 */
@Dao
interface OccupationsDao {
    /**
     * Searches Occupation by Id
     */
    @Query("SELECT * FROM occupations WHERE id LIKE :substring LIMIT :maxCount")
    fun searchByString(substring: String, maxCount: Int): List<Occupations>
}