package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Resource
import kso.repo.search.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: AppRepository
) :
    ViewModel() {

    private val tag: String = "HomePageViewModel"
    private val repoName: String = savedStateHandle.get<String>("repo_name").orEmpty()
    val searchText: MutableStateFlow<String> = MutableStateFlow(repoName)

    private val repoListResponseResourceSharedFlow = MutableSharedFlow<Unit>()

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class)
    var repoListResponseResource = repoListResponseResourceSharedFlow
        .map { searchText.value }
        .flatMapLatest { repository.getSearchRepoList(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    init {
        Log.e(tag, "init");
        Log.e(tag, "Argument: $repoName")
        Log.e(tag, "SearchText: ${searchText.value}")

        submit()

    }


    private fun submit() {
        Log.e(tag, "getDefaultRepoList()")

        viewModelScope.launch {
            Log.e(tag, "in ViewModelScope")
            repoListResponseResourceSharedFlow.emit(Unit)
        }
    }

    fun retry() {
        Log.e(tag, "Retry:")
        submit()
    }

    fun onSearchTextChanged(changedSearchText: String) {

        Log.e(tag, "onSearchTextChanged: keywordt ${searchText.value}")
        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
           return
        }
        submit()

    }

    fun onSearchBoxClear() {
        Log.e(tag, "onSearchClear: ")
        searchText.value = ""
        submit()
    }


}