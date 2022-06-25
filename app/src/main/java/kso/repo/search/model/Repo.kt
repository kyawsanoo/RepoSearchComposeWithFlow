package kso.repo.search.model
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Repo(

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("full_name")
    val fullName: String= "",

    @SerializedName("owner")
    val owner: User? = null,

    @SerializedName("html_url")
    val htmlUrl: String= "",

    @SerializedName("description")
    val description: String?=null,

    @SerializedName("url")
    val url: String= "",

    @SerializedName("clone_url")
    val cloneUrl: String = "",

    @SerializedName("watchers_count")
    val watchers_count: Int = 0,

    @SerializedName("watchers")
    val watchers: Int = 0,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("forks_count")
    val forksCount: Int = 0,

    @SerializedName("forks")
    val forks: Int=0,

    @SerializedName("open_issues")
    val openIssues: Int = 0,

    @SerializedName("open_issues_count")
    val openIssuesCount: Int = 0,

    @SerializedName("default_branch")
    val defaultBranch: String=""

  /*
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("updated_at")
    val updatedAt: Date,
    @SerializedName("pushed_at")
    val pushedAt: Date,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("ssh_url")
    val sshUrl: String,
    @SerializedName("clone_url")
    val cloneUrl: String,
    @SerializedName("svn_url")
    val svnUrl: String,
    val homepage: String?,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("has_issues")
    val hasIssues: Boolean,
    @SerializedName("has_downloads")
    val hasDownloads: Boolean,
    @SerializedName("has_wiki")
    val hasWiki: Boolean,
    @SerializedName("has_pages")
    val hasPages: Boolean,
    */
)