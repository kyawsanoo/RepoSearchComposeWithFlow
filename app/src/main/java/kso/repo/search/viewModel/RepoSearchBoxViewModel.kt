package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Repo
import kso.repo.search.repository.AppRepository
import kso.repo.search.ui.state.RepoSearchBoxViewModelState
import javax.inject.Inject

private const val TAG = "RepoSearchBoxViewModel"

@HiltViewModel
class RepoSearchBoxViewModel @Inject constructor(private val userRepository: AppRepository) : ViewModel() {

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedRepos: MutableStateFlow<List<Repo>> =  MutableStateFlow(arrayListOf())

    val repoSearchModelState = combine(
        searchText,
        matchedRepos,
        showProgressBar
    ) { text, matchedRepos, showProgress ->

        RepoSearchBoxViewModelState(
            text,
            matchedRepos,
            showProgress
        )
    }

    init {
       gitHubRepos()
    }

    @OptIn(FlowPreview::class)
    private fun gitHubRepos() {
        Log.e(TAG, "init")
    }


    fun onSearchTextChanged(changedSearchText: String) {

        Log.e(TAG, "onSearchTextChanged: keywordt ${searchText.value}")
        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
            matchedRepos.value = arrayListOf()
            return
        }
        viewModelScope.launch {
            showProgressBar.value = true
            val usersFromSearch = userRepository.getSearchRepoList(searchText.value).map {it.data.orEmpty()}//mapping to repoList
                .flatMapConcat { it.asFlow() }
                .toList()
            showProgressBar.value = false
            matchedRepos.value = usersFromSearch

        }


    }

    fun onSearchBoxClear() {
        Log.e(TAG, "onSearchClear: ")
        searchText.value = ""
        matchedRepos.value = arrayListOf()
    }


}

