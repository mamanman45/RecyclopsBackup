package com.example.recyclops.ui.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val MAXIMAL_SIZE = 1000000

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun rotateFile(file: File, isBackCamera: Boolean = false) {
    val exif = ExifInterface(file.path)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    val rotation: Float = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun getTrivia(wasteType: String): String {
    val plastik = "Plastik adalah sampah yang materialnya diproduksi dari bahan kimia tak terbarukan. Sampah sampah plastik dapat terurai di tanah 1000 tahun lamanya, sedangkan kantong plastik 10 hingga 1000 tahun. Botol plastik dapat terurai di alam sekitar 450 tahun."
    val kaca = "Sampah kaca adalah yang berasal dari pecahan kaca botol bekas minuman, parfum, pecahan kaca mobil maupun jendela rumah. Sampah jenis ini memerlukan waktu hingga 1.000.000 tahun untuk terurai sepenuhnya, jika tidak segera di daur ulang."
    val kardus = "Kardus atau karton box merupakan salah satu material yang punya karakteristik ringan juga praktis. Barangnya yang mudah didapat dan terjangkau menjadikan kardus pilihan terbaik sebagai wadah kemasan dan penyimpanan jika dibandingkan dengan plastik."
    val logam = "Limbah logam merupakan limbah yang mudah dipisahkan dari timbunan sampah dan dapat didaur ulang menjadi barang – barang yang bernilai seni, dilebur kembali sebagai menjadi material asalnya, dan juga dapat dimanfaatkan sebagai campuran semen dan sebagainya."
    val kertas = "Limbah kertas merupakan bahan buangan sisa proses produksi maupun pemakaian yang mengandung berbagai komponen seperti selulosa, hemiselulosa, lignin, bahan ekstraktif, larutan Cl2, hidrogen peroksida, asam parasetat, dan sebagainya."

    if (wasteType.equals("Plastik", ignoreCase = true)){
        return plastik
    } else if (wasteType.equals("Kaca", ignoreCase = true)){
        return kaca
    } else if (wasteType.equals("Kardus", ignoreCase = true)){
        return kardus
    } else if (wasteType.equals("Logam", ignoreCase = true)){
        return logam
    } else if (wasteType.equals("Kertas", ignoreCase = true)){
        return kertas
    } else {
        return "Error"
    }
}

