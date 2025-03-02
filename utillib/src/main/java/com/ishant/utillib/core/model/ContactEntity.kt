package com.ishant.utillib.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "contacts")
data class ContactEntity(
    @NotNull
    @PrimaryKey(autoGenerate = true)
    val phone: String ,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "countryCode")
    val countryCode: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "birthday")
    val birthday: String?,
    @ColumnInfo(name = "photoUri")
    val photoUri: String?
)

