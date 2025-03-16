package com.techhub.util.core.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigHelper @Inject constructor(){
    private val _fetchingStatus = MutableLiveData(FetchStatus.LOADING)
    val fetchingStatus: LiveData<FetchStatus>
        get() = _fetchingStatus

    enum class FetchStatus { SUCCESSFUL, LOADING, ERROR }
    private val remoteConfig by lazy {
        val settings = remoteConfigSettings {
            fetchTimeoutInSeconds = 30
            minimumFetchIntervalInSeconds = 200
        }
        // Initialize Remote Config default values.
        val defaults = mutableMapOf<String, Any>(
            /*"IS_WATCH_AD_TO_UNLOCK_FONTS_ENABLED" to true,
            "IS_WATCH_AD_TO_UNLOCK_PHOTOS_ENABLED" to true*/
        )
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(settings)
            setDefaultsAsync(defaults)
        }

    }

    fun loadSettings() {
        _fetchingStatus.value = FetchStatus.LOADING
        remoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                _fetchingStatus.postValue(FetchStatus.SUCCESSFUL)
            }.addOnFailureListener {
                _fetchingStatus.postValue(FetchStatus.ERROR)
            }
    }
    private fun getBaseConfigRef(): FirebaseRemoteConfig {
        return remoteConfig
    }

    fun getStringValueFromRemoteConfig(param: String): String {
        return remoteConfig.getString(param) ?: ""
    }

    fun getLongValueFromRemoteConfig(param: String): Long {
        return remoteConfig.getLong(param) ?: 0L
    }

    fun getBooleanValueFromRemoteConfig(param: String): Boolean {
        return remoteConfig.getBoolean(param) ?: true
    }
    fun getDoubleValueFromRemoteConfig(param: String): Double {
        return remoteConfig.getDouble(param) ?: 0.0
    }


    object RemoteConfigConstants {
        const val IS_LIBRARY_ALLOWED = "is_library_allowed"
        const val LIB_VERSION = "version"
    }
}