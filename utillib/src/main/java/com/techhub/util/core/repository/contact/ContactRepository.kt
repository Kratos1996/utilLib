package com.techhub.util.core.repository.contact

import androidx.paging.PagingData
import com.techhub.util.core.model.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getPaginatedContacts(): Flow<PagingData<ContactEntity>>
    suspend fun saveContacts(contacts: List<ContactEntity>)
}