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

    val TAG: String = "HomePageViewModel"
    private val login: String = savedStateHandle.get<String>("login").orEmpty()

    val userName = MutableStateFlow(login)

    private val responseSharedFlow = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val responseResource = responseSharedFlow
        .map { userName.value }
        .flatMapLatest { repository.getRepoList(it) }
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

    fun setUserName(userName: String) {
        this.userName.value = userName
    }

    fun onResume() {
        Log.e(TAG, "Resume:");
        Log.e(TAG, "Argument:" + login);
        viewModelScope.launch {
            responseSharedFlow.emit(Unit)
        }
    }

    fun retry() {
        Log.e(TAG, "Retry Click")
        viewModelScope.launch {
            responseSharedFlow.emit(Unit)
        }
    }


}