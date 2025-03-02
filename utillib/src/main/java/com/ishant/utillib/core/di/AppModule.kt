package com.ishant.utillib.core.di

import android.content.Context
import androidx.room.Room
import com.ishant.utillib.core.ContactHelper
import com.ishant.utillib.core.database.ContactDatabase
import com.ishant.utillib.core.database.dao.ContactDao
import com.ishant.utillib.core.repository.contact.ContactRepository
import com.ishant.utillib.core.repository.contact.ContactRepositoryImpl
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