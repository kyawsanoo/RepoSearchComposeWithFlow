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
    private val networkStatusDetector: NetworkStatusDetector
) :
    ViewModel() {

    private val tag: String = "HomePageViewModel"
    private val repoName: String = savedStateHandle.get<String>("repo_name").orEmpty()

    val searchText: MutableStateFlow<String> = MutableStateFlow(repoName)

    private var repoListNBRSharedFlow = MutableSharedFlow<Unit>()

    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(ExperimentalCoroutinesApi::class)
    var repoListNBR = repoListNBRSharedFlow
        .map {
            searchText.value
        }
        .flatMapLatest { repository.getRepoListNetworkBoundResource(it)}
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)


    @OptIn(FlowPreview::class)
    public val networkState =
        networkStatusDetector.networkStatus
            .map (
                onAvailable = { NetworkConnectionState.Fetched },
                onUnavailable = { NetworkConnectionState.Error },
            )

    val isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {

        Log.e(tag, "init");
        Log.e(tag, "Argument: $repoName")
        Log.e(tag, "SearchText: ${searchText.value}")

        submit()

    }


    @OptIn(FlowPreview::class)
    fun submit() {
        Log.e(tag, "fetch RepoList")

        viewModelScope.launch {
            Log.e(tag, "in ViewModelScope")
            repoListNBRSharedFlow.emit(Unit)

            networkState.collect{
                        networkState ->
                isConnected.value = when (networkState) {
                    NetworkConnectionState.Fetched -> {
                        Log.e(tag, "Network Status: Fetched")
                        true
                    }
                    else -> {
                        Log.e(tag, "Network Status: Error")
                        false
                    }
                }
            }

        }

    }

    fun retry() {
        Log.e(tag, "Retry:")
        submit()
    }

    fun refresh(){
        Log.e(tag, "Refresh:")
        isRefreshing.value = true
        submit()
        isRefreshing.value = false
    }

    fun onDoneCollectResource(){
        Log.e(tag, "onDoneCollectResource()")
        isRefreshing.value = false
    }

}