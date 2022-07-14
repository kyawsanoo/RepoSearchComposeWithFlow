package kso.repo.search.dataSource.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kso.repo.search.model.Owner

class Converters {

        private val gson = Gson()

        @TypeConverter
        fun ownerToString(owner: Owner): String {
            return gson.toJson(owner)
        }

        @TypeConverter
        fun stringToOwner(recipeString: String): Owner {
            val objectType = object : TypeToken<Owner>() {}.type
            return gson.fromJson(recipeString, objectType)
        }

}