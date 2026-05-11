package com.simats.formsahayak.logic

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream

class FormDownloader(private val context: Context) {

    fun downloadFormWithMarkings(
        originalBitmap: Bitmap,
        detectedFields: List<DetectedField>,
        formType: String
    ) {
        try {
            // Create a mutable copy of the bitmap to draw on
            val markedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(markedBitmap)
            
            // Setup Paint for Red Boxes
            val boxPaint = Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 8f
            }

            // Setup Paint for Labels (Optional: adding text labels to the saved image)
            val textPaint = Paint().apply {
                color = Color.RED
                textSize = 40f
                isFakeBoldText = true
            }

            // Draw each detected field on the bitmap
            detectedFields.forEach { field ->
                canvas.drawRect(field.bounds, boxPaint)
                // Draw field name slightly above the box
                canvas.drawText(field.name, field.bounds.left.toFloat(), (field.bounds.top - 10).toFloat(), textPaint)
            }

            saveBitmapToStorage(markedBitmap, "FormSahayak_${System.currentTimeMillis()}")
            
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBitmapToStorage(bitmap: Bitmap, filename: String) {
        val resolver = context.contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Download/FormSahayak")
            }
        }

        var imageUri: Uri? = null
        var outputStream: OutputStream? = null

        try {
            imageUri = resolver.insert(imageCollection, contentValues)
            if (imageUri != null) {
                outputStream = resolver.openOutputStream(imageUri)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
                Toast.makeText(context, "Form downloaded successfully to Download/FormSahayak", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            if (imageUri != null) {
                resolver.delete(imageUri, null, null)
            }
            Toast.makeText(context, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            outputStream?.close()
        }
    }
    
    fun getDownloadedForms(): List<Uri> {
        val forms = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        } else {
            "${MediaStore.Images.Media.DATA} LIKE ?"
        }
        val selectionArgs = arrayOf("%Download/FormSahayak%")

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                forms.add(contentUri)
            }
        }
        return forms
    }
}
