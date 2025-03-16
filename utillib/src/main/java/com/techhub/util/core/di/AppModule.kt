package com.techhub.util.core.di

import android.content.Context
import com.techhub.util.core.ContactHelper
import com.techhub.util.core.database.ContactDatabase
import com.techhub.util.core.database.dao.ContactDao
import com.techhub.util.core.repository.contact.ContactRepository
import com.techhub.util.core.repository.contact.ContactRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getDatabase(context = context)
    }

    @Provides
    fun provideContactDao(database: ContactDatabase): ContactDao = database.contactDao()

    @Provides
    fun provideContactRepository(dao: ContactDao): ContactRepository = ContactRepositoryImpl(dao)
    @Provides
    fun provideContactHelper(repository: ContactRepository): ContactHelper = ContactHelper(repository = repository)

}