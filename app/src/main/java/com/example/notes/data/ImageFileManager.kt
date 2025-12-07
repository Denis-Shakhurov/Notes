package com.example.notes.data

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ImageFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val imageDir: File = context.filesDir

    suspend fun copeImageToInternalStorage(url: String) : String {
        val fileName = "IMG_${UUID.randomUUID()}.jpg"
        val file = File(imageDir, fileName)

        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(url.toUri()).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
            }
        }

        return file.absolutePath
    }

    suspend fun deleteImageInInternalStorage(url: String) {
        withContext(Dispatchers.IO) {
            val file = File(url)

            if (file.exists() && isInternal(file.absolutePath)) {
                file.delete()
            }
        }
    }

    fun isInternal(uri: String) : Boolean {
        return uri.startsWith(imageDir.absolutePath)
    }
}