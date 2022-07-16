package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Resource
import kso.repo.search.repository.KeywordSearchBaseRepository
import javax.inject.Inject


@HiltViewModel
class KeywordSearchPageViewModel @Inject constructor(
    private val appRepository: KeywordSearchBaseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val tag: String = "KeywordPageViewModel"
    private val repoName: String = savedStateHandle.get<String>("repo").orEmpty()

    val searchText: MutableStateFlow<String> = MutableStateFlow(repoName)


    private val keywordListShareFlow = MutableSharedFlow<Unit>()

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class)
    var keywordListNBR = keywordListShareFlow
        .map { searchText.value }
        .flatMapLatest { appRepository.getKeywordListNetworkBoundResource(it)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    init {

        Log.e(tag, "init")
        Log.e(tag, "Argument: $repoName")
        Log.e(tag, "SearchText: ${searchText.value}")

        submit()

    }


    private fun submit() {
        Log.e(tag, "fetchAllKeywordList()")

        viewModelScope.launch {
            Log.e(tag, "in ViewModelScope")
            keywordListShareFlow.emit(Unit)
        }

    }

    fun retry() {
        Log.e(tag, "Retry:")
        submit()
    }

    fun onSearchTextChanged(changedSearchText: String) {

        Log.e(tag, "onSearchTextChanged: keyword ${searchText.value}")
        searchText.value = changedSearchText
        submit()

    }

    fun onSearchBoxClear() {
        Log.e(tag, "onSearchClear: ")
        searchText.value = ""
        submit()
    }

}
