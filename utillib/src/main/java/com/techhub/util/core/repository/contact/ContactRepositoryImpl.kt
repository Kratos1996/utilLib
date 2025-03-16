package com.techhub.util.core.repository.contact

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.techhub.util.core.database.dao.ContactDao
import com.techhub.util.core.model.ContactEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val dao: ContactDao
): ContactRepository  {
    override fun getPaginatedContacts(): Flow<PagingData<ContactEntity>> {
         return Pager(
           config = PagingConfig(pageSize = 20, enablePlaceholders = false),
           pagingSourceFactory = { dao.getContactsPaged() }
       ).flow
    }

    override suspend fun saveContacts(contacts: List<ContactEntity>) {
        dao.insertContacts(contacts)
    }
}