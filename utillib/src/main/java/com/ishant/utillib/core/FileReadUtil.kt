package com.ishant.utillib.core

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object FileReadUtil {
    interface ReadCallback {
        fun onSuccess(data: ByteArray)
        fun onFailure(exception: Exception)
    }

    fun readFromUri(context: Context, uri: Uri, callback: ReadCallback) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val data = inputStream.readBytes()
                callback.onSuccess(data)
            } ?: callback.onFailure(IOException("Failed to open input stream"))
        } catch (e: IOException) {
            callback.onFailure(e)
        }
    }

    fun readVideoFiles(context: Context, callback: ReadCallback) {
        readMediaFiles(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, callback)
    }

    fun readImageFiles(context: Context, callback: ReadCallback) {
        readMediaFiles(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, callback)
    }

    fun readAudioFiles(context: Context, callback: ReadCallback) {
        readMediaFiles(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, callback)
    }

    fun readDocumentFiles(context: Context, callback: ReadCallback) {
        readMediaFiles(context, MediaStore.Files.getContentUri("external"), callback)
    }

    private fun readMediaFiles(context: Context, uri: Uri, callback: ReadCallback) {
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(columnIndex)
                val file = File(filePath)
                try {
                    FileInputStream(file).use { inputStream ->
                        val data = inputStream.readBytes()
                        callback.onSuccess(data)
                    }
                } catch (e: IOException) {
                    callback.onFailure(e)
                }
            }
        }
    }
}
