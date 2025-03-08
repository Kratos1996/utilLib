package com.ishant.utillib.core


import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileWriteUtil {
    interface WriteCallback {
        fun onSuccess(uri: Uri?)
        fun onFailure(exception: Exception)
        fun onProgress(progress: Int)
    }



    // Writing data using getExternalFilesDir() (App-Specific Storage)
    private fun writeToAppSpecificStorage(context: Context, fileName: String, data: ByteArray) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Writing data using Storage Access Framework (SAF)
    fun openSAFForWriting(launcher: ActivityResultLauncher<Intent>, fileName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/octet-stream"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        launcher.launch(intent)
    }


    // Writing data to a Custom Path (Requires WRITE_EXTERNAL_STORAGE permission for API < 29)
    fun writeToCustomPath(context: Context, filePath: String, data: ByteArray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!PermissionUtil.CheckPermission.hasWriteExternalStoragePermission(context)) {
                return // Permission required for API < 29
            }
        } else {
            // Fallback to app-specific storage for unsupported versions
            writeToAppSpecificStorage(context, File(filePath).name, data)
            return
        }
    }


    fun writeToSAFStorage(context: Context, uri: Uri, data: ByteArray) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(data)
        }
    }


    // Writing data to a Custom Path (Requires WRITE_EXTERNAL_STORAGE permission for API < 29)
    fun writeToCustomPath(filePath: String, data: ByteArray) {
        val file = File(filePath)
        try {
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { outputStream ->
                outputStream.write(data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Writing data using MediaStore API (Public Storage)
    fun writeToPublicStorage(
        context: Context,
        fileName: String,
        data: ByteArray,
        mimeType: String,
        callback: WriteCallback
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        val chunkSize = 1024
                        var written = 0
                        while (written < data.size) {
                            val remaining = data.size - written
                            val toWrite = remaining.coerceAtMost(chunkSize)
                            outputStream.write(data, written, toWrite)
                            written += toWrite
                            callback.onProgress((written * 100) / data.size)
                        }
                    }
                }
                callback.onSuccess(uri)
            } else {
                writeToAppSpecificStorage(context, fileName, data, callback)
            }
        } catch (e: IOException) {
            callback.onFailure(e)
        }
    }

    private fun writeToAppSpecificStorage(
        context: Context,
        fileName: String,
        data: ByteArray,
        callback: WriteCallback
    ) {
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            FileOutputStream(file).use { outputStream ->
                val chunkSize = 1024
                var written = 0
                while (written < data.size) {
                    val remaining = data.size - written
                    val toWrite = remaining.coerceAtMost(chunkSize)
                    outputStream.write(data, written, toWrite)
                    written += toWrite
                    callback.onProgress((written * 100) / data.size)
                }
            }
            callback.onSuccess(Uri.fromFile(file))
        } catch (e: IOException) {
            callback.onFailure(e)
        }
    }
}


// Example Usage
/*

// Example Usage in an Activity
class FileWriteActivity : Activity() {
    private lateinit var safLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        safLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    writeToSAF(uri)
                }
            }
        }
    }

    private fun writeToSAF(uri: Uri) {
        val sampleData = "Hello, World!".toByteArray()
        FileWriteUtil.writeToSAFStorage(this, uri, sampleData)
    }

    fun saveFile() {
        val sampleData = "Hello, World!".toByteArray()

        // Using MediaStore API (Public Storage)
        FileWriteUtil.writeToPublicStorage(this, "example.txt", sampleData, "text/plain")

        // Using getExternalFilesDir() (App-Specific Storage)
        FileWriteUtil.writeToAppSpecificStorage(this, "example.txt", sampleData)

        // Using SAF (Storage Access Framework)
        FileWriteUtil.openSAFForWriting(safLauncher, "example.txt")

        // Using Custom Path (Ensure necessary permissions for API < 29)
        FileWriteUtil.writeToCustomPath("/storage/emulated/0/MyCustomFolder/example.txt", sampleData)
          val sampleData = "Hello, this is a test file".toByteArray()
        val fileName = "testFile.txt"

        FileWriteUtil.writeToPublicStorage(this, fileName, sampleData, "text/plain", object : FileWriteUtil.WriteCallback {
            override fun onSuccess(uri: Uri?) {
                Toast.makeText(this@FileWriteActivity, "File saved successfully: $uri", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(this@FileWriteActivity, "Failed to save file: ${exception.message}", Toast.LENGTH_LONG).show()
            }

            override fun onProgress(progress: Int) {
                Log.d("FileWrite", "Progress: $progress%")
            }
        })
    }
    }
}
*/
