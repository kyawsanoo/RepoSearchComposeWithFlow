package kso.repo.search.dataSource.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

private const val SEARCH_KEYWORD = "search_keyword"

class PreferenceProvider @Inject constructor(
    context: Context
) {

    private val applicationContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    fun setSearchKeyword(query: String) {
        preference.edit().putString(
            SEARCH_KEYWORD,
            query
        ).apply()
    }

    fun getSearchKeyword() : String = preference.getString(SEARCH_KEYWORD, "")!!
}