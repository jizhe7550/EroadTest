package com.eroadtest.eroadtest.util

import android.os.Environment
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.OutputModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.Exception

class FileHelper {

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun getExternalFile(filename:String):File{
        return File(MyApplication.applicationContext().getExternalFilesDir(null), filename)
    }

    fun writeFile(filename: String, outputModelJson:String){
        if (!isExternalStorageWritable())
            throw Exception(EXTERNAL_NO_SPACE)

        val myFile = getExternalFile(filename)
        try {
            myFile.bufferedWriter().use { writer ->
                writer.write(outputModelJson)
            }
        } catch (e: Exception) {
            throw Exception("file $filename wrote fail!")
        }
    }

    fun listRecordFileNames():List<String>?{
        var file = MyApplication.applicationContext().getExternalFilesDir(null)
        var files = file?.listFiles()
        return files?.map {
            it.name
        }
    }

    fun listRecordFiles(): Array<out File>? {
        var file = MyApplication.applicationContext().getExternalFilesDir(null)
        return file?.listFiles()
    }

    fun readFile(filename: String): OutputModel {
        var outputModel: OutputModel
        try {
            val myFile = getExternalFile(filename)
            myFile.bufferedReader().use { reader ->
                val outputModelType = object : TypeToken<OutputModel>() {}.type
                outputModel = Gson().fromJson(reader, outputModelType)
            }
            return outputModel
        } catch (e: Exception) {
            throw Exception("file $filename read fail!")
        }
    }

    companion object {
        const val EXTERNAL_NO_SPACE = "Cannot record data"
    }
}