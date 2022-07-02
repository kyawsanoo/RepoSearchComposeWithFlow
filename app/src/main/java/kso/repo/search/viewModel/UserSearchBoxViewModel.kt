package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.User
import kso.repo.search.repository.AppRepository
import kso.repo.search.ui.state.SearchBoxViewModelState
import javax.inject.Inject

private const val TAG = "SearchBoxViewModel"

@HiltViewModel
class SearchBoxViewModel @Inject constructor(private val userRepository: AppRepository) : ViewModel() {

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedUsers: MutableStateFlow<List<User>> =  MutableStateFlow(arrayListOf())

    val userSearchModelState = combine(
        searchText,
        matchedUsers,
        showProgressBar
    ) { text, matchedUsers, showProgress ->

        SearchBoxViewModelState(
            text,
            matchedUsers,
            showProgress
        )
    }

    init {
        Log.e(TAG, "init")
    }

    fun onSearchTextChanged(changedSearchText: String) {

        Log.e(TAG, "onSearchTextChanged: keywordt ${searchText.value}")
        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
            matchedUsers.value = arrayListOf()
            return
        }
        viewModelScope.launch {
            showProgressBar.value = true
            val users: List<User> = userRepository.getSearchUserList(searchText.value)
                .map {it.data.orEmpty()}//mapping to userList
                .flatMapConcat { it.asFlow() }
                .toList()
            if (users.isNotEmpty()) {
                matchedUsers.value = users
            }
            showProgressBar.value = false
        }


    }

    fun onSearchBoxClear() {
        Log.e(TAG, "onSearchClear: ")
        searchText.value = ""
        matchedUsers.value = arrayListOf()
    }


}

