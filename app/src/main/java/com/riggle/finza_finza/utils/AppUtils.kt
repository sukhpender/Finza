package com.riggle.finza_finza.utils

import android.content.Context
import android.os.Environment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun textToRequestBody(text: String?): RequestBody {
        return text!!.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    @Throws(IOException::class)
    fun createImageFile1(context: Context): File? {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".png",  /* suffix */
            storageDir /* directory */
        )
        return image
    }
}