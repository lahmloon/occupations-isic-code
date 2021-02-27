package com.lahmloon.occupations_isic_code_sdk.occupations.data

import androidx.room.*

/**
 * Occupation entity
 */
@Entity(
        tableName = "occupations",
)
data class Occupations(
        @ColumnInfo(name = "id")
        @PrimaryKey
        val id: String,
        @ColumnInfo(name = "id_sub_of")
        val idSubOf: String?,
        @ColumnInfo(name = "name_th")
        val nameTh: String?,
        @ColumnInfo(name = "name_eng")
        val nameEng: String?,
)