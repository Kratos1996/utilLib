package com.ishant.util.viewmodel

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.ishant.utillib.core.ContactHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val helper: ContactHelper,
    private val application: Application
) : ViewModel() {


    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun fetchAndSaveContacts() {
        helper.fetchAndSaveContacts(application)
    }
    fun getAllContacts() = helper.getAllContacts()
}