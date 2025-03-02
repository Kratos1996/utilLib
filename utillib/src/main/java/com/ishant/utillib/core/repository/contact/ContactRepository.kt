package com.ishant.utillib.core.repository.contact

import androidx.paging.PagingData
import com.ishant.utillib.core.model.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getPaginatedContacts(): Flow<PagingData<ContactEntity>>
    suspend fun saveContacts(contacts: List<ContactEntity>)
}