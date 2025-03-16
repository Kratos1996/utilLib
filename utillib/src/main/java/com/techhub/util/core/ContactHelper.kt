package com.techhub.util.core

import android.Manifest
import android.app.Application
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.techhub.util.core.model.ContactEntity
import com.techhub.util.core.repository.contact.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactHelper @Inject constructor(val repository: ContactRepository){

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun fetchAndSaveContacts(application: Application) {
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = getContacts(application.applicationContext)
            repository.saveContacts(contacts)
        }
    }
    suspend fun saveContact(
        name: String,
        phoneNumber: String,
        context: Context
    ): Boolean {
        return try {
            val operations = ArrayList<ContentProviderOperation>()

            // Insert the contact's display name
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )

            // Insert the contact's name
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name
                    )
                    .build()
            )

            // Insert the contact's phone number
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )

            // Apply the batch of operations
            val contentResolver = context.contentResolver
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)

            true // Return true if successful
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false if an error occurs
        }
    }

    fun getAllContacts() = repository.getPaginatedContacts()
    private fun getContacts(context: Context): List<ContactEntity> {
        val contactsList = mutableListOf<ContactEntity>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
            val contactIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

            while (it.moveToNext()) {
                val contactId = it.getString(contactIdIndex) ?: continue
                val name = it.getString(nameIndex) ?: "Unknown"
                val phoneNumber = it.getString(numberIndex) ?: "No Number"
                val photoUri = it.getString(photoIndex)

                val email = getEmail(contentResolver, contactId)
                val birthday = getBirthday(contentResolver, contactId)
                val countryCode = getCountryCode(phoneNumber)
                contactsList.add(
                    ContactEntity(
                        name = name,
                        phone = phoneNumber,
                        countryCode = countryCode,
                        email = email,
                        birthday = birthday,
                        photoUri = photoUri
                    )
                )
            }
        }
        cursor?.close()
        return contactsList
    }

    private fun getEmail(contentResolver: ContentResolver, contactId: String): String? {
        val emailCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        )
        var email: String? = null
        emailCursor?.use {
            if (it.moveToFirst()) {
                email = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))?:""
            }
        }
        emailCursor?.close()
        return email
    }

    private fun getBirthday(contentResolver: ContentResolver, contactId: String): String? {
        val birthdayCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
            null
        )
        var birthday: String? = null
        birthdayCursor?.use {
            if (it.moveToFirst()) {
                birthday = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
            }
        }
        birthdayCursor?.close()
        return birthday
    }

    private fun getCountryCode(phone: String): String? {
        return phone.takeWhile { it.isDigit() }.take(3) // Extract first 3 digits (Example logic)
    }
}