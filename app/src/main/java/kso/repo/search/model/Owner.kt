package kso.repo.search.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Owner", indices = [Index("repoId")])
data class Owner(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val ownerId: Long? = null,

    @SerializedName("login")
    val login: String? = "",

    @SerializedName("avatar_url")
    val avatarUrl: String? = "",

    @SerializedName("gravatar_id")
    val gravatarId: String? = "",

    @SerializedName("url")
    val ownerUrl: String? = "",

    @SerializedName("html_url")
    val ownerHtmlUrl: String? = "",

    @SerializedName("followers_url")
    val followersUrl: String? = "",

    @SerializedName("following_url")
    val followingUrl: String? = "",

    @SerializedName("gists_url")
    val gistsUrl: String? = "",

    @SerializedName("starred_url")
    val starredUrl: String? = "",

    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String? = "",

    @SerializedName("organizations_url")
    val organizationsUrl: String? = "",

    @SerializedName("repos_url")
    val reposUrl: String? = "",

    @SerializedName("events_url")
    val eventsUrl: String? = "",

    @SerializedName("received_events_url")
    val receivedEventsUrl: String? = "",

    @SerializedName("type")
    val type: String? = "",

    var repoId: Long? = 0L

)