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
class HomePageViewModel @Inject constructor(savedStateHandle: SavedStateHandle, repository: AppRepository):
    ViewModel(){

    private val TAG: String = "HomePageViewModel"
    private val login: String = savedStateHandle.get<String>("repo_name").orEmpty()

    val repoName = MutableStateFlow(login)

    private val responseSharedFlow = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val responseResource = responseSharedFlow
        .map { repoName.value }
        .flatMapLatest { repository.getSearchRepoList(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    val errorMessage = responseResource.map {
        when(it.errorMessage.orEmpty().contains("java.net.UnknownHostException")){
            true -> "Please check your connection and retry"
            else -> {
                it.errorMessage.orEmpty()
            }
        }
    }


   /* init {
        Log.e(TAG, "Resume:");
        Log.e(TAG, "Argument:" + login);
        viewModelScope.launch {
            responseSharedFlow.emit(Unit)
        }
    }*/


    fun onResume() {
        Log.e(TAG, "onResume:")
        Log.e(TAG, "Argument: $login")
        viewModelScope.launch {
            Log.e(TAG, "in ViewModelScope")
            responseSharedFlow.emit(Unit)
        }
    }

    fun retry() {
        Log.e(TAG, "Retry:")
        viewModelScope.launch {
            Log.e(TAG, "in ViewModelScope")
            responseSharedFlow.emit(Unit)
        }
    }


}