package com.ishant.utillib.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ishant.utillib.core.model.ContactEntity
@Dao
interface ContactDao {
    @Transaction
    suspend fun insertContactsTransaction(contacts: List<ContactEntity>) {
        deleteAllContacts()
        insertContacts(contacts)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getContactsPaged(): PagingSource<Int, ContactEntity>
}
