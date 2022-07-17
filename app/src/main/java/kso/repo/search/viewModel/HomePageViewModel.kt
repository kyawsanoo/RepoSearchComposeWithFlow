package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() :  ViewModel() {

    private val tag: String = "HomePageViewModel"
    val searchText: MutableStateFlow<String> = MutableStateFlow("")


    init {
        Log.e(tag, "init")
        Log.e(tag, "SearchText: ${searchText.value}")
    }

    fun onSearchTextChanged(changedSearchText: String) {
        Log.e(tag, "onSearchTextChanged: keyword ${searchText.value}")
        searchText.value = changedSearchText
    }

    fun onKeyboardSearchClick(query: String) {
        Log.e(tag, "onSearchTextChanged: keyword ${searchText.value}")
        searchText.value = query
    }

    fun onSearchBoxClear() {
        Log.e(tag, "onSearchClear: ")
        searchText.value = ""
    }

}