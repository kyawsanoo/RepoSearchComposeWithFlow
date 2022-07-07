package kso.repo.search.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "keywords")
data class Keyword(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("name")
    val name: String? = "",


)
