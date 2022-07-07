package kso.repo.search.dataSource.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kso.repo.search.model.User

class Converters {

        private val gson = Gson()

        @TypeConverter
        fun ownerToString(owner: User): String {
            return gson.toJson(owner)
        }

        @TypeConverter
        fun stringToOwner(recipeString: String): User {
            val objectType = object : TypeToken<User>() {}.type
            return gson.fromJson(recipeString, objectType)
        }

}