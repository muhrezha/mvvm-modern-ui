package co.id.rezha.core.utlis

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RoomConverters {

    @TypeConverter
    fun fromString(value: String?): List<Long>? {
        if (value == null) return null
        val type = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: List<Long>?): String? {
        return Gson().toJson(list)
    }
}
