package ro.upt.ac.tooler.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters{
    @TypeConverter
    fun convertToolListToJSONString(toolList: List<Int>?): String {
        return if(toolList.isNullOrEmpty())
            ""
        else
            Gson().toJson(toolList)
    }
    @TypeConverter
    fun convertJSONStringToToolList(jsonString: String): List<Int> {
        return if (jsonString.isNotEmpty()) {Gson().fromJson(jsonString,object : TypeToken<List<Int>>() {}.type)}
        else {
            emptyList()
        }
    }

}