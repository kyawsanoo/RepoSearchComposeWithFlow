package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.app.NetworkStatusDetector
import kso.repo.search.app.map
import kso.repo.search.model.Resource
import kso.repo.search.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppRepository,
    networkStatusDetector: NetworkStatusDetector
) :
    ViewModel() {

    private val tag: String = "HomePageViewModel"
    private val repoName: String = savedStateHandle.get<String>("repo_name").orEmpty()

    val searchText: MutableStateFlow<String> = MutableStateFlow(repoName)

    private var repoListNBRSharedFlow = MutableSharedFlow<Unit>()

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class)
    var repoListNBR = repoListNBRSharedFlow
        .map { searchText.value }
        .flatMapLatest { repository.getRepoListNetworkBoundResource(it)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)


    @OptIn(FlowPreview::class)
    val changedNetworkStatus =
        networkStatusDetector.networkStatus
            .map (
                onAvailable = { NetworkConnectionState.Fetched },
                onUnavailable = { NetworkConnectionState.Error },
            )

    init {

        Log.e(tag, "init");
        Log.e(tag, "Argument: $repoName")
        Log.e(tag, "SearchText: ${searchText.value}")

        submit()

    }


    fun submit() {
        Log.e(tag, "fetch RepoList")

        viewModelScope.launch {
            Log.e(tag, "in ViewModelScope")

            repoListNBRSharedFlow.emit(Unit)
        }

    }

    fun retry() {
        Log.e(tag, "Retry:")
        submit()
    }

}