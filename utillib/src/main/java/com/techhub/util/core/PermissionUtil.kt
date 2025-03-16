package com.techhub.util.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {
    fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

     fun shouldShowRationale(context: Context, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            (context as? android.app.Activity) ?: return false,
            permission
        )
    }
    fun requestPermission(context: Context,permission: String, requestCode: Int) {
        if (!isPermissionGranted(context,permission)) {
            ActivityCompat.requestPermissions((context as android.app.Activity), arrayOf(permission), requestCode)
        }
    }


    @Composable
    fun RequestPermission(
        context: Context,
        permission: String,
        onPermissionResult: (Boolean) -> Unit,
        onShowRationale: (() -> Unit)? = null
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                onPermissionResult(isGranted)
            }

        LaunchedEffect(Unit) {
            if (PermissionUtil.shouldShowRationale(context, permission)) {
                onShowRationale?.invoke()
            } else {
                launcher.launch(permission)
            }
        }
    }

    @Composable
    fun RequestMultiplePermissions(
        context: Context,
        permissions: Array<String>,
        onPermissionResult: (Map<String, Boolean>) -> Unit,
        onShowRationale: (() -> Unit)? = null
    ) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                onPermissionResult(results)
            }

        LaunchedEffect(Unit) {
            if (permissions.any { PermissionUtil.shouldShowRationale(context, it) }) {
                onShowRationale?.invoke()
            } else {
                launcher.launch(permissions)
            }
        }
    }

    object RequestPermission {

        @Composable
        fun RequestCompleteStoragePermissions(
            context: Context,
            onPermissionResult: (Map<String, Boolean>) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            }
            RequestMultiplePermissions(context, permissions, onPermissionResult, onShowRationale)
        }

        @Composable
        fun RequestCameraPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.CAMERA,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestRecordAudioPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.RECORD_AUDIO,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestReadStoragePermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }

        @Composable
        fun RequestWriteStoragePermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }

        @Composable
        fun RequestFineLocationPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestCoarseLocationPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestReadContactsPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.READ_CONTACTS,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestWriteContactsPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.WRITE_CONTACTS,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestCallPhonePermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.CALL_PHONE,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestSendSmsPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.SEND_SMS,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestReceiveSmsPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            RequestPermission(
                context,
                Manifest.permission.RECEIVE_SMS,
                onPermissionResult,
                onShowRationale
            )
        }

        @Composable
        fun RequestPostNotificationPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }

        @Composable
        fun RequestReadMediaAudioPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }

        @Composable
        fun RequestReadMediaImagesPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }

        @Composable
        fun RequestReadMediaVideoPermission(
            context: Context,
            onPermissionResult: (Boolean) -> Unit,
            onShowRationale: (() -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    onPermissionResult,
                    onShowRationale
                )
            }
        }
    }

    object CheckPermission {
        fun hasWriteExternalStoragePermission(context: Context): Boolean {
            return isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        fun hasReadExternalStoragePermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        fun hasCameraPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.CAMERA)

        fun hasRecordAudioPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.RECORD_AUDIO)

        fun hasFineLocationPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

        fun hasCoarseLocationPermission(context: Context) = isPermissionGranted(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun hasReadContactsPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.READ_CONTACTS)

        fun hasWriteContactsPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.WRITE_CONTACTS)

        fun hasCallPhonePermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.CALL_PHONE)

        fun hasSendSmsPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.SEND_SMS)

        fun hasReceiveSmsPermission(context: Context) =
            isPermissionGranted(context, Manifest.permission.RECEIVE_SMS)

        fun hasPostNotificationPermission(context: Context) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && PermissionUtil.isPermissionGranted(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )

        private fun hasReadMediaAudioPermission(context: Context) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isPermissionGranted(
                context,
                Manifest.permission.READ_MEDIA_AUDIO
            )

        private fun hasReadMediaImagesPermission(context: Context) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isPermissionGranted(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            )

        private fun hasReadMediaVideoPermission(context: Context) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isPermissionGranted(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            )

         fun hasReadAudioPermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionUtil.CheckPermission.hasReadMediaAudioPermission(context)
            } else {
                PermissionUtil.CheckPermission.hasReadExternalStoragePermission(context)
            }
        }
        fun hasReadVideoPermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionUtil.CheckPermission.hasReadMediaVideoPermission(context)
            } else {
                PermissionUtil.CheckPermission.hasReadExternalStoragePermission(context)
            }
        }
        fun hasReadImagePermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionUtil.CheckPermission.hasReadMediaVideoPermission(context)
            } else {
                PermissionUtil.CheckPermission.hasReadExternalStoragePermission(context)
            }
        }
    }


}

// Example Usage:
/*
@Composable
fun MyScreen(context: Context) {
    RequestCameraPermission(
        context = context,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with camera functionality
            } else {
                // Permission denied, show explanation if needed
            }
        },
        onShowRationale = {
            // Show a dialog explaining why the permission is needed
        }
    )
}
*/