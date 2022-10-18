package com.el3asas.note.data.source.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteEntityConverter {

    @TypeConverter
    fun setImages(images :List<String?>?) : String? = Gson().toJson(images)

    @TypeConverter
    fun getImages(images :String?) : List<String?>? =
        if(images!=null) try {
            Gson().fromJson(images ,Array<String?>::class.java).toList()
        }catch (ignored:Exception){
            null
        } else null

}