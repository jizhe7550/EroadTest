package com.eroadtest.eroadtest.util

import android.content.Context
import android.os.Environment
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.OutputModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlin.Exception

class FileHelper constructor(
    private val context: Context = MyApplication.applicationContext()
) {

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun getExternalFile(filename:String):File{
        return File(context.getExternalFilesDir(null), filename)
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
        var file = context.getExternalFilesDir(null)
        var files = file?.listFiles()
        return files?.map {
            it.name
        }
    }

    fun listRecordFiles(): Array<out File>? {
        var file = context.getExternalFilesDir(null)
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

    /**
     * get date String from filename, format like Sensor_YYYY-MM-dd-hh-mm-ss-SSS.sns
     *
     * for example, filename Sensor_YYYY-MM-dd-hh-mm-ss-SSS.sns will return YYYY-MM-dd-hh-mm-ss-SSS
     */
    fun getDateStrFromSnsFileName(filename: String):String{
        try {
            return filename.substring(7, filename.length - 4)
        }catch (e:Exception){
            throw Exception(WRONG_FORMAT_FOR_FILENAME)
        }
    }

    companion object {
        const val EXTERNAL_NO_SPACE = "Cannot record data"
        const val WRONG_FORMAT_FOR_FILENAME = "Wrong format for filename"
    }
}